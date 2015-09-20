package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Nation extends Model {
  public abstract String name();
  public abstract String large_image();
  public abstract String small_image();

  public String remoteImageLarge() {
    return large_image().replace("localhost", "10.0.3.2");
  }
  public String remoteImageSmall() {
    return small_image().replace("localhost", "10.0.3.2");
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Nation.typeAdapterFactory();
  }

  @Override public String toString() {
    return name();
  }

  @Override public int describeContents() {
    return 0;
  }
}
