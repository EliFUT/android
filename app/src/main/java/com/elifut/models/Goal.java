package com.elifut.models;

import com.google.auto.value.AutoValue;

import android.os.Parcelable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Goal implements Parcelable, MatchEvent {
  @Override public abstract int time();
  public abstract Club club();
  public abstract Player player();

  public static Goal create(int time, Club club, Player player) {
    return new AutoValue_Goal(time, club, player);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static JsonAdapter<Goal> jsonAdapter(Moshi moshi) {
    return new AutoValue_Goal.MoshiJsonAdapter(moshi);
  }
}
