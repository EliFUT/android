package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

import java.util.List;

@AutoValue
public abstract class Club extends Model implements Parcelable {
  public abstract String name();
  public abstract League league();


  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Club.typeAdapterFactory();
  }

  // needed workaround for now.
  @Override public int describeContents() {
    return 0;
  }
}
