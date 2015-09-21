package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Club extends Model {
  public abstract String name();
  @Nullable public abstract String abbrev_name();
  public abstract String small_image();
  public abstract String large_image();
  public abstract int league_id();

  public String shortName() {
    return abbrev_name() != null ? abbrev_name() : name();
  }

  public String remoteImageLarge() {
    return large_image().replace("localhost", "10.0.3.2");
  }

  public String remoteImageSmall() {
    return small_image().replace("localhost", "10.0.3.2");
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Club.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
