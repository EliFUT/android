package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class League extends Model {
  public abstract String name();
  @Nullable public abstract String abbrev_name();
  public abstract String image();

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_League.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
