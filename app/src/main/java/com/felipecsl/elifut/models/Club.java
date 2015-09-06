package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Club extends Model implements Parcelable {
  public abstract String name();
  public abstract String image();

  public String remoteImage() {
    return image().replace("localhost", "10.0.3.2");
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Club.typeAdapterFactory();
  }

  // needed workaround for now.
  @Override public int describeContents() {
    return 0;
  }
}
