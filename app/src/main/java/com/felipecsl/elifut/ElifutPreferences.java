package com.felipecsl.elifut;

import android.content.SharedPreferences;
import android.util.Log;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public final class ElifutPreferences {
  private static final String TAG = "ElifutPreferences";
  private final Moshi moshi;
  private final SharedPreferences sharedPreferences;

  public ElifutPreferences(SharedPreferences sharedPreferences, Moshi moshi) {
    this.sharedPreferences = sharedPreferences;
    this.moshi = moshi;
  }

  public void storeCoachName(String name) {
    sharedPreferences.edit()
        .putString("COACH_NAME", name)
        .apply();
  }

  public void storeUserNation(Nation nation) {
    storeObject(nation, "UserNation");
  }

  public void storeUserClub(Club club) {
    storeObject(club, "UserClub");
  }

  public void storeUserLeague(League league) {
    storeObject(league, "UserLeague");
  }

  public Club retrieveUserClub() {
    return retrieveObject(Club.class, "UserClub");
  }

  public Nation retrieveUserNation() {
    return retrieveObject(Nation.class, "UserNation");
  }

  public League retrieveUserLeague() {
    return retrieveObject(League.class, "UserLeague");
  }

  private <T> void storeObject(T object, String key) {
    Class<T> klass = (Class<T>) object.getClass();
    String objectJson = moshi.adapter(klass).toJson(object);
    sharedPreferences.edit()
        .putString(key, objectJson)
        .apply();
  }

  private <T> T retrieveObject(Class<T> type, String key) {
    String objectJson = sharedPreferences.getString(key, null);
    if (objectJson == null) {
      return null;
    }
    try {
      return moshi.adapter(type).fromJson(objectJson);
    } catch (IOException e) {
      Log.e(TAG, "failed to retrieve object of class " + key, e);
      return null;
    }
  }
}
