package com.felipecsl.elifut.preferences;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.felipecsl.elifut.models.Club;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.shuffle;
import static com.felipecsl.elifut.util.CollectionUtils.toList;

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

  public void putClubsAndInitMatches(Club userClub, Observable<Club> observable) {
    clubsPreference.set(toList(observable));
    List<Club> otherClubs = toList(observable.filter((c) -> !c.equals(userClub)));

    if (opponentsPreference.get() == null) {
      opponentsPreference.set(shuffle(otherClubs));
    }
  }

  public Club popAndUpdateNextMatch() {
    List<Club> nextOpponents = opponentsPreference.get();
    if (nextOpponents == null || nextOpponents.isEmpty()) {
      throw new IllegalStateException("no opponents");
    }
    Club club = nextOpponents.remove(0);
    opponentsPreference.set(nextOpponents);
    return club;
  }

  public JsonPreference<List<Club>> clubsPreference() {
    return clubsPreference;
  }

  public JsonPreference<List<Club>> opponentsPreference() {
    return opponentsPreference;
  }
}
