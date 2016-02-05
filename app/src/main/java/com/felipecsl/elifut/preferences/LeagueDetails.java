package com.felipecsl.elifut.preferences;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.services.LeagueRoundGenerator;

import java.util.List;

import rx.Observable;

public final class LeagueDetails {
  private final ElifutDataStore service;
  private final LeagueRoundGenerator leagueRoundGenerator;
  private final MatchResultGenerator generator;

  public LeagueDetails(ElifutDataStore persistenceService,
      LeagueRoundGenerator leagueRoundGenerator, MatchResultGenerator generator) {
    this.service = persistenceService;
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

  /** Adds MatchResults to the list of matches on the provided LeagueRound */
  public LeagueRound executeRound(LeagueRound round) {
    List<Match> matchesWithResult = FluentIterable.from(round.matches()).transform(m ->
        Match.create(m.id(), m.home(), m.away(), generator.generate(m.home(), m.away()))).toList();
    return LeagueRound.create(round.id(), round.roundNumber(), matchesWithResult);
  }

  public Observable<? extends List<? extends Club>> clubsObservable() {
    return service.observe(AutoValueClasses.CLUB);
  }

  public Observable<? extends List<? extends LeagueRound>> roundsObservable() {
    return service.observe(AutoValueClasses.LEAGUE_ROUND);
  }

  public List<? extends LeagueRound> rounds() {
    return service.query(AutoValueClasses.LEAGUE_ROUND);
  }
}
