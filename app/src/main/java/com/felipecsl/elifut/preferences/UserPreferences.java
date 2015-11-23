package com.felipecsl.elifut.preferences;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

public final class UserPreferences {
  private static final String KEY_USER_CLUB = "UserClub";
  private static final String KEY_USER_NATION = "UserNation";
  private static final String KEY_USER_LEAGUE = "UserLeague";
  private static final String KEY_COACH_NAME = "COACH_NAME";

  private final JsonPreference<Nation> nationPreference;
  private final JsonPreference<Club> clubPreference;
  private final JsonPreference<League> leaguePreference;
  private final JsonPreference<String> coachPreference;

  public UserPreferences(SharedPreferences preferences, Moshi moshi) {
    RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(preferences);
    JsonAdapter<Nation> nationAdapter = moshi.adapter(Nation.class);
    JsonAdapter<Club> clubAdapter = moshi.adapter(Club.class);
    JsonAdapter<League> leagueAdapter = moshi.adapter(League.class);
    JsonAdapter<String> stringAdapter = moshi.adapter(String.class);
    nationPreference = new JsonPreference<>(rxSharedPreferences, nationAdapter, KEY_USER_NATION);
    clubPreference = new JsonPreference<>(rxSharedPreferences, clubAdapter, KEY_USER_CLUB);
    leaguePreference = new JsonPreference<>(rxSharedPreferences, leagueAdapter, KEY_USER_LEAGUE);
    coachPreference = new JsonPreference<>(rxSharedPreferences, stringAdapter, KEY_COACH_NAME);
  }

  public JsonPreference<Nation> nationPreference() {
    return nationPreference;
  }

  public JsonPreference<Club> clubPreference() {
    return clubPreference;
  }

  public JsonPreference<League> leaguePreference() {
    return leaguePreference;
  }

  public JsonPreference<String> coachPreference() {
    return coachPreference;
  }
}
