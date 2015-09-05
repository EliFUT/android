package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Nation extends Model implements Parcelable {

  public abstract String name();
  public abstract String image();

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Nation.typeAdapterFactory();
  }

  // needed workaround for now.
  @Override public int describeContents() {
    return 0;
  }

  @Override public String toString() {
    return name();
  }
}
