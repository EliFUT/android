package com.felipecsl.elifut.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Club extends Model implements Parcelable {
  public abstract String name();
  @Nullable public abstract String abbrev_name();
  public abstract String small_image();
  public abstract String large_image();
  public abstract int league_id();

  public static Club create(
      int id, String name, String smallImage, String largeImage, int leagueId) {
    return new AutoValue_Club(id, id, name, null, smallImage, largeImage, leagueId);
  }

  public String shortName() {
    return abbrev_name() != null ? abbrev_name() : name();
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Club.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
