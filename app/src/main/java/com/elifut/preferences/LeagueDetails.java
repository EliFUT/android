package com.elifut.preferences;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

import com.elifut.AutoValueClasses;
import com.elifut.match.MatchResultGenerator;
import com.elifut.models.Club;
import com.elifut.models.ClubStats;
import com.elifut.models.LeagueRound;
import com.elifut.models.Match;
import com.elifut.models.MatchResult;
import com.elifut.services.ClubDataStore;
import com.elifut.services.ElifutDataStore;
import com.elifut.services.LeagueRoundGenerator;
import com.elifut.util.CollectionUtilsKt;

import java.util.List;

import rx.Observable;

public final class LeagueDetails {
  private final ElifutDataStore service;
  private final ClubDataStore clubDataStore;
  private final LeagueRoundGenerator leagueRoundGenerator;
  private final MatchResultGenerator generator;

  public LeagueDetails(ElifutDataStore persistenceService, ClubDataStore clubDataStore,
      LeagueRoundGenerator leagueRoundGenerator, MatchResultGenerator generator) {
    this.service = persistenceService;
    this.clubDataStore = clubDataStore;
    this.leagueRoundGenerator = leagueRoundGenerator;
    this.generator = generator;
  }

  /**
   * Initializes the league details by storing the list of clubs into persistent storage and
   * generating each rounds by randomizing the clubs and matching them against each other twice.
   */
  public void initialize(List<? extends Club> allClubs) {
    service.create(allClubs);
    service.create(leagueRoundGenerator.generateRounds(allClubs));
  }

  /** Removes the next round from the list of upcoming rounds and returns it. */
  public LeagueRound nextRound() {
    List<? extends LeagueRound> nextRounds = rounds();
    if (nextRounds == null || nextRounds.isEmpty()) {
      throw new IllegalStateException("No more rounds left");
    }
    LeagueRound nextRound = nextRounds.remove(0);
    //noinspection ConstantConditions
    service.delete(nextRound, nextRound.id());
    return nextRound;
  }

  /** Returns the position of the provided Club in the League current standings */
  public int clubPosition(Club club) {
    List<? extends Club> sortedClubs = clubsStandings();
    Optional<? extends Club> selectedClub = FluentIterable.from(sortedClubs)
        .firstMatch(c -> c.nameEquals(club));
    return selectedClub.isPresent() ? sortedClubs.indexOf(selectedClub.get()) : -1;
  }

  /**
   * Returns the list of Clubs in this League in the correct order according to the current
   * stadings.
   */
  public List<? extends Club> clubsStandings() {
    return clubsStandings(clubs());
  }

  private static List<? extends Club> clubsStandings(List<? extends Club> clubs) {
    return CollectionUtilsKt.sort(clubs, (lhs, rhs) -> {
      ClubStats rhsStats = rhs.nonNullStats();
      ClubStats lhsStats = lhs.nonNullStats();
      int pointsDifference = rhsStats.points() - lhsStats.points();
      int goalsDifference = rhsStats.goals() - lhsStats.goals();
      return pointsDifference != 0 ? pointsDifference : goalsDifference;
    });
  }

  /** Adds MatchResults to the list of matches on the provided LeagueRound */
  public LeagueRound executeRound(LeagueRound round) {
    List<Match> matchesWithResult = FluentIterable.from(round.matches())
        .transform(m -> Match.create(m.id(), m.home(), m.away(), resultFor(m)))
        .toList();
    return LeagueRound.create(round.id(), round.roundNumber(), matchesWithResult);
  }

  private MatchResult resultFor(Match m) {
    return generator.generate(
        m.home(), clubDataStore.squad(m.home()), m.away(), clubDataStore.squad(m.away()));
  }

  /**
   * Returns an Observable that emits events with the updated list of clubs in this League. The
   * returned list is already sorted by club position on the League standings.
   */
  public Observable<? extends List<? extends Club>> clubsObservable() {
    return service.observe(AutoValueClasses.CLUB)
        .map(LeagueDetails::clubsStandings);
  }

  public Observable<? extends List<? extends LeagueRound>> roundsObservable() {
    return service.observe(AutoValueClasses.LEAGUE_ROUND);
  }

  /** Returns all clubs for the current league */
  public List<? extends Club> clubs() {
    return service.query(AutoValueClasses.CLUB);
  }

  /** Returns all rounds for the current league */
  public List<? extends LeagueRound> rounds() {
    return service.query(AutoValueClasses.LEAGUE_ROUND);
  }
}
