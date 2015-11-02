package com.felipecsl.elifut;

import android.content.SharedPreferences;
import android.util.Log;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.Nation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

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

  public void storeLeagueClubs(List<Club> clubs) {
    JsonAdapter<List<Club>> adapter =
        moshi.adapter(Types.newParameterizedType(List.class, Club.class));
    storeObject(adapter, clubs, "LC");
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

  public List<Club> retrieveLeagueClubs() {
    JsonAdapter<?> adapter = moshi.adapter(Types.newParameterizedType(List.class, Club.class));
    List<Club> clubs = retrieveObject(adapter, "LC");

    if (clubs == null) {
      return Collections.emptyList();
    }
    return clubs;
  }

  private <T> void storeObject(T object, String key) {
    Class<T> klass = (Class<T>) object.getClass();
    storeObject(moshi.adapter(klass), object, key);
  }

  private <T> void storeObject(JsonAdapter<T> adapter, T object, String key) {
    String objectJson = adapter.toJson(object);
    sharedPreferences.edit()
        .putString(key, objectJson)
        .apply();
  }

  private <T> T retrieveObject(Type type, String key) {
    return retrieveObject(moshi.adapter(type), key);
  }

  private <T> T retrieveObject(JsonAdapter<?> adapter, String key) {
    String objectJson = sharedPreferences.getString(key, null);
    if (objectJson == null) {
      return null;
    }
    try {
      return (T) adapter.fromJson(objectJson);
    } catch (IOException e) {
      Log.e(TAG, "failed to retrieve object of class " + key, e);
      return null;
    }
  }
}
