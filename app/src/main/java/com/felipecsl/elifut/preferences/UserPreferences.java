package com.felipecsl.elifut.preferences;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;

public final class UserPreferences {
  private static final String KEY_USER_CLUB = "UserClub";
  private static final String KEY_USER_NATION = "UserNation";
  private static final String KEY_USER_LEAGUE = "UserLeague";
  private static final String KEY_COACH_NAME = "COACH_NAME";
  private final ElifutPreferences preferences;

  public UserPreferences(ElifutPreferences preferences) {
    this.preferences = preferences;
  }

  public void putNation(Nation nation) {
    preferences.putObject(nation, Nation.class, KEY_USER_NATION);
  }

  public void putClub(Club club) {
    preferences.putObject(club, Club.class, KEY_USER_CLUB);
  }

  public Club club() {
    return preferences.getObject(Club.class, KEY_USER_CLUB);
  }

  public Nation nation() {
    return preferences.getObject(Nation.class, KEY_USER_NATION);
  }

  public void putLeague(League league) {
    preferences.putObject(league, League.class, KEY_USER_LEAGUE);
  }

  public League league() {
    return preferences.getObject(League.class, KEY_USER_LEAGUE);
  }

  public void putCoachName(String name) {
    preferences.putString(KEY_COACH_NAME, name);
  }

  public String coachName() {
    return preferences.getString(KEY_COACH_NAME);
  }
}
