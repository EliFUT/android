package com.felipecsl.elifut.preferences;

import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.reflect.Type;

public class ElifutPreferences {
  private static final String TAG = "ElifutPreferences";
  private final Moshi moshi;
  private final SharedPreferences sharedPreferences;

  public ElifutPreferences(SharedPreferences sharedPreferences, Moshi moshi) {
    this.sharedPreferences = sharedPreferences;
    this.moshi = moshi;
  }

  <T> void putObject(T object, Type type, String key) {
    putObject(moshi.adapter(type), object, key);
  }

  <T> void putObject(JsonAdapter<T> adapter, T object, String key) {
    String objectJson = adapter.toJson(object);
    sharedPreferences.edit()
        .putString(key, objectJson)
        .apply();
  }

  <T> T getObject(Type type, String key) {
    return getObject(moshi.adapter(type), key);
  }

  <T> T getObject(JsonAdapter<?> adapter, String key) {
    String objectJson = getString(key);
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

  void putString(String key, String value) {
    sharedPreferences.edit()
        .putString(key, value)
        .apply();
  }

  String getString(String key) {
    return sharedPreferences.getString(key, null);
  }
}
