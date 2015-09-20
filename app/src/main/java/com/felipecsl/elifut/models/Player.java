package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Player extends Model {
  public abstract String first_name();
  public abstract String last_name();
  public abstract String name();
  @Nullable public abstract String common_name();
  public abstract String position();
  public abstract String image();
  public abstract String nation_image();
  public abstract int rating();
  public abstract String player_type();
  public abstract int attribute_1();
  public abstract int attribute_2();
  public abstract int attribute_3();
  public abstract int attribute_4();
  public abstract int attribute_5();
  public abstract int attribute_6();
  public abstract String quality();
  public abstract String color();

  public String nationRemoteImageSmall() {
    return nation_image().replace("localhost", "10.0.3.2");
  }

  enum Foot {
    Left, Right
  }

  public String remoteImage() {
    // TODO: fix this
    return image().replace("localhost", "10.0.3.2");
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Player.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
