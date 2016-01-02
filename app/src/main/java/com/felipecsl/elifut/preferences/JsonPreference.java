package com.felipecsl.elifut.preferences;

import android.support.annotation.Nullable;
import android.util.Log;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.squareup.moshi.JsonAdapter;

import java.io.IOException;

public final class JsonPreference<T> {
  private static final String TAG = JsonPreference.class.getSimpleName();
  private final Preference<String> rxPreference;
  private final JsonAdapter<T> adapter;

  JsonPreference(
      RxSharedPreferences rxSharedPreferences, JsonAdapter<T> adapter, String key) {
    this.adapter = adapter;
    this.rxPreference = rxSharedPreferences.getString(key);
  }

  @Nullable public T get() {
    return map(rxPreference.get());
  }

  private T map(String objectJson) {
    if (objectJson == null) {
      return null;
    }
    try {
      return adapter.fromJson(objectJson);
    } catch (IOException e) {
      Log.e(TAG, "failed to retrieve object");
      return null;
    }
  }

  public T set(T object) {
    rxPreference.set(adapter.toJson(object));
    return object;
  }
}
