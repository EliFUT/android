package com.felipecsl.elifut.preferences;

import com.felipecsl.elifut.Util;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.services.ElifutPersistenceService;

import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.shuffle;

public final class LeaguePreferences {
  private final ElifutPersistenceService service;
  private final Class<? extends Club> clubType = Util.autoValueTypeFor(Club.class);
  private final Class<? extends LeagueRound> leagueRoundType =
      Util.autoValueTypeFor(LeagueRound.class);

  public LeaguePreferences(ElifutPersistenceService persistenceService) {
    this.service = persistenceService;
  }

  public void putClubsAndInitRounds(List<Club> allClubs) {
    service.create(allClubs);
    service.create(League.generateRounds(shuffle(allClubs)));
  }

  /** Removes the next round from the list of upcoming rounds and returns it */
  public LeagueRound nextRound() {
//    List<LeagueRound> nextRounds = roundsPreference.get();
//    if (nextRounds == null || nextRounds.isEmpty()) {
//      throw new IllegalStateException("No more rounds left");
//    }
//    LeagueRound round = nextRounds.remove(0);
//    roundsPreference.set(nextRounds);
//    return round;
    return null;
  }

  public Observable<? extends List<? extends Club>> clubsObservable() {
    return service.observable(clubType);
  }

  public Observable<? extends List<? extends LeagueRound>> roundsObservable() {
    return service.observable(leagueRoundType);
  }

  public List<? extends LeagueRound> rounds() {
    return service.query(leagueRoundType);
  }
}
