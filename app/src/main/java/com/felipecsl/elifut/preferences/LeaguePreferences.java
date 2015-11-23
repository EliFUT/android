package com.felipecsl.elifut.preferences;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;
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
  private final JsonPreference<List<Club>> clubsPreference;
  private final JsonPreference<List<Club>> opponentsPreference;

  public LeaguePreferences(SharedPreferences preferences, Moshi moshi) {
    RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(preferences);
    ParameterizedType type = Types.newParameterizedType(List.class, Club.class);
    JsonAdapter<List<Club>> adapter = moshi.adapter(type);
    clubsPreference = new JsonPreference<>(rxSharedPreferences, adapter, KEY_CLUBS);
    opponentsPreference = new JsonPreference<>(rxSharedPreferences, adapter, KEY_OPPONENTS);
  }

  public void putClubsAndInitOpponents(Club userClub, Observable<Club> observable) {
    putLeagueClubs(observable.toList().toBlocking().first());
    List<Club> otherClubs = observable.filter((c) -> !c.equals(userClub))
        .toList()
        .toBlocking()
        .first();

    Collections.shuffle(otherClubs);

    if (opponentsPreference.get() == null) {
      opponentsPreference.set(otherClubs);
    }
  }

  public Observable<List<Club>> clubsObservable() {
    return clubsPreference.asObservable();
  }

  public List<Club> clubs() {
    return clubsPreference.get();
  }

  public JsonPreference<List<Club>> nextOpponentsPreference() {
    return opponentsPreference;
  }

  public Club popAndUpdateNextOpponents() {
    List<Club> nextOpponents = opponentsPreference.get();
    if (nextOpponents == null || nextOpponents.isEmpty()) {
      throw new IllegalStateException("no opponents");
    }
    Club club = nextOpponents.remove(0);
    opponentsPreference.set(nextOpponents);
    return club;
  }

  public void putLeagueClubs(Observable<Club> clubs) {
    putLeagueClubs(clubs.toList().toBlocking().first());
  }

  public void putLeagueClubs(List<Club> clubs) {
    clubsPreference.set(clubs);
  }
}
