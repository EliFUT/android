package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Nation extends Model {
  public abstract String name();
  public abstract String image();

  public String remoteImage() {
    return image().replace("localhost", "10.0.3.2");
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Nation.typeAdapterFactory();
  }

  @Override public String toString() {
    return name();
  }
}
