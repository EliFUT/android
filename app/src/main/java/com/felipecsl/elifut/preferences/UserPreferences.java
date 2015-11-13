package com.felipecsl.elifut.preferences;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;

public final class UserPreferences {
  private static final String KEY_USER_CLUB = "UserClub";
  private static final String KEY_USER_NATION = "UserNation";
  private static final String KEY_USER_LEAGUE = "UserLeague";
  private final ElifutPreferences preferences;

  public UserPreferences(ElifutPreferences preferences) {
    this.preferences = preferences;
  }

  public void putUserNation(Nation nation) {
    preferences.putObject(nation, Nation.class, KEY_USER_NATION);
  }

  public void putUserClub(Club club) {
    preferences.putObject(club, Club.class, KEY_USER_CLUB);
  }

  public Club getUserClub() {
    return preferences.getObject(Club.class, KEY_USER_CLUB);
  }

  public Nation getUserNation() {
    return preferences.getObject(Nation.class, KEY_USER_NATION);
  }

  public void storeUserLeague(League league) {
    preferences.putObject(league, League.class, KEY_USER_LEAGUE);
  }

  public League getUserLeague() {
    return preferences.getObject(League.class, KEY_USER_LEAGUE);
  }

  public void putCoachName(String name) {
    preferences.putString("COACH_NAME", name);
  }
}
