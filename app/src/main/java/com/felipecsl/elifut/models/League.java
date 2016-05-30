package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;

import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class League extends Model {
  public abstract String name();

  @Nullable public abstract String abbrev_name();

  public abstract String image();

  public static JsonAdapter<League> jsonAdapter(Moshi moshi) {
    return new AutoValue_League.MoshiJsonAdapter(moshi);
  }

  @Override public int describeContents() {
    return 0;
  }
}
