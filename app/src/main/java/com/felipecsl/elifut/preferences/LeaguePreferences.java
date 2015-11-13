package com.felipecsl.elifut.preferences;

import com.felipecsl.elifut.models.Club;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import rx.Observable;

public final class LeaguePreferences {
  private static final String KEY_CLUBS = "LC";
  private static final String KEY_OPPONENTS = "OPPONENTS";
  private final ElifutPreferences preferences;
  private final Moshi moshi;

  public LeaguePreferences(ElifutPreferences preferences, Moshi moshi) {
    this.preferences = preferences;
    this.moshi = moshi;
  }

  public void putClubsAndInitOpponentsIfNeeded(Club userClub, Observable<Club> observable) {
    putLeagueClubs(observable.toList().toBlocking().first());
    List<Club> otherClubs = observable.filter((c) -> !c.equals(userClub))
        .toList()
        .toBlocking()
        .first();

    Collections.shuffle(otherClubs);

    if (getNextOpponents() == null) {
      putNextOpponents(otherClubs);
    }
  }

  public Observable<Club> getLeagueClubs() {
    List<Club> clubs = getClubsByKey(KEY_CLUBS);

    if (clubs == null || clubs.isEmpty()) {
      return Observable.empty();
    }
    return Observable.from(clubs);
  }

  public List<Club> getNextOpponents() {
    return getClubsByKey(KEY_OPPONENTS);
  }

  private void putNextOpponents(List<Club> clubs) {
    putClubsByKey(clubs, KEY_OPPONENTS);
  }

  public void putLeagueClubs(Observable<Club> clubs) {
    putLeagueClubs(clubs.toList().toBlocking().first());
  }

  public void putLeagueClubs(List<Club> clubs) {
    putClubsByKey(clubs, KEY_CLUBS);
  }

  private void putClubsByKey(List<Club> observable, String key) {
    ParameterizedType type = Types.newParameterizedType(List.class, Club.class);
    JsonAdapter<List<Club>> adapter = moshi.adapter(type);
    preferences.putObject(adapter, observable, key);
  }

  private List<Club> getClubsByKey(String key) {
    ParameterizedType type = Types.newParameterizedType(List.class, Club.class);
    JsonAdapter<List<Club>> adapter = moshi.adapter(type);
    return preferences.getObject(adapter, key);
  }
}
