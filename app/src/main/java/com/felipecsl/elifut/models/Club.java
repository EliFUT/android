package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Club extends Model {
  public abstract String name();
  @Nullable public abstract String abbrev_name();
  public abstract String image();

  public String shortName() {
    return abbrev_name() != null ? abbrev_name() : name();
  }

  public String remoteImage() {
    return image().replace("localhost", "10.0.3.2");
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Club.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
