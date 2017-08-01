package com.elifut.preferences;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.elifut.models.Club;
import com.elifut.models.League;
import com.elifut.models.Nation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

public final class UserPreferences {
  private static final String KEY_USER_CLUB = "UserClub";
  private static final String KEY_USER_NATION = "UserNation";
  private static final String KEY_USER_LEAGUE = "UserLeague";
  private static final String KEY_COACH_NAME = "COACH_NAME";
  private static final String KEY_GAME_SPEED = "KEY_GAME_SPEED";
  private static final String KEY_COINS = "COINS";
  // Game speed multiplier:
  //  1 means 1 game minute equals to 1 second in wall clock.
  //  2 means 1 game minute equals to 0.5 seconds in wall clock.
  private static final int DEFAULT_GAME_SPEED = 1;

  public static final long INITIAL_COINS_AMOUNT = 200L;
  public static final long COINS_PRIZE_WIN = 75L;
  public static final long COINS_PRIZE_DRAW = 25L;
  public static final long COINS_PRIZE_LOSS = 0L;

  private final JsonPreference<Nation> nationPreference;
  private final JsonPreference<Club> clubPreference;
  private final JsonPreference<League> leaguePreference;
  private final Preference<String> coachPreference;
  private final Preference<Integer> gameSpeedPreference;
  private final Preference<Long> coinsPreference;

  public UserPreferences(SharedPreferences preferences, Moshi moshi) {
    RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(preferences);
    JsonAdapter<Nation> nationAdapter = moshi.adapter(Nation.class);
    JsonAdapter<Club> clubAdapter = moshi.adapter(Club.class);
    JsonAdapter<League> leagueAdapter = moshi.adapter(League.class);
    nationPreference = new JsonPreference<>(rxSharedPreferences, nationAdapter, KEY_USER_NATION);
    clubPreference = new JsonPreference<>(rxSharedPreferences, clubAdapter, KEY_USER_CLUB);
    leaguePreference = new JsonPreference<>(rxSharedPreferences, leagueAdapter, KEY_USER_LEAGUE);
    coachPreference = rxSharedPreferences.getString(KEY_COACH_NAME);
    coinsPreference = rxSharedPreferences.getLong(KEY_COINS);
    gameSpeedPreference = rxSharedPreferences.getInteger(KEY_GAME_SPEED, DEFAULT_GAME_SPEED);
  }

  public Club club() {
    return clubPreference.get();
  }

  public Nation nation() {
    return nationPreference.get();
  }

  public League league() {
    return leaguePreference.get();
  }

  public String coach() {
    return coachPreference.get();
  }

  public Long coins() {
    return coinsPreference.get();
  }

  public Integer gameSpeed() {
    return gameSpeedPreference.get();
  }

  public boolean isCurrentUserClub(Club club) {
    return club().equals(club);
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

  public Preference<String> coachPreference() {
    return coachPreference;
  }

  public Preference<Long> coinsPreference() {
    return coinsPreference;
  }

  public Preference<Integer> gameSpeedPreference() {
    return gameSpeedPreference;
  }
}
