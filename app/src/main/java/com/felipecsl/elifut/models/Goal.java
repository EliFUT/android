package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Goal implements Parcelable, MatchEvent {
  @Override public abstract int time();
//  public abstract Player player();
  public abstract Club club();

  public static Goal create(int time, Club club) {
    return new AutoValue_Goal(time, club);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Goal.typeAdapterFactory();
  }
}
