package com.felipecsl.elifut.preferences;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.services.ElifutPersistenceService;
import com.felipecsl.elifut.services.LeagueRoundGenerator;

import java.util.List;

import rx.Observable;

public final class LeagueDetails {
  private final ElifutPersistenceService service;
  private final LeagueRoundGenerator leagueRoundGenerator;

  public LeagueDetails(ElifutPersistenceService persistenceService,
      LeagueRoundGenerator leagueRoundGenerator) {
    this.service = persistenceService;
    this.leagueRoundGenerator = leagueRoundGenerator;
  }

  /**
   * Initializes the league details by storing the list of clubs into persistent storage and
   * generating each rounds by randomizing the clubs and matching them against each other twice.
   */
  public void initialize(List<? extends Club> allClubs) {
    service.create(allClubs);
    service.create(leagueRoundGenerator.generateRounds(allClubs));
  }

  /** Removes the next round from the list of upcoming rounds and returns it */
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

  public Observable<? extends List<? extends Club>> clubsObservable() {
    return service.observable(AutoValueClasses.CLUB);
  }

  public Observable<? extends List<? extends LeagueRound>> roundsObservable() {
    return service.observable(AutoValueClasses.LEAGUE_ROUND);
  }

  public List<? extends LeagueRound> rounds() {
    return service.query(AutoValueClasses.LEAGUE_ROUND);
  }
}
