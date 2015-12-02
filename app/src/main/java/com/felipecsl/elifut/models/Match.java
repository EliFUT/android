package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Match implements Parcelable {
  public abstract Club home();
  public abstract Club away();

  public static Match create(Club home, Club away) {
    return new AutoValue_Match(home, away);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public String toString() {
    return home().name() + " X " + away().name();
  }

  public boolean hasClub(Club club) {
    return home().nameEquals(club) || away().nameEquals(club);
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Match.typeAdapterFactory();
  }
}
