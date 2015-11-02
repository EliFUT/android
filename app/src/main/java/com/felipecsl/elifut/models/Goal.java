package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Goal implements Parcelable, MatchEvent {
  public abstract int time();
  public abstract Club club();

  public static Goal create(int time, Club club) {
    return new AutoValue_Goal(time, club);
  }

  public static Creator<? extends Goal> creator() {
    return AutoValue_Goal.CREATOR;
  }

  @Override public int describeContents() {
    return 0;
  }
}
