package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class League implements Parcelable {
  public abstract String name();
  public abstract Nation nation();

  // needed workaround for now.
  @Override public int describeContents() {
    return 0;
  }
}
