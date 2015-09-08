package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Player extends Model {
  public abstract int id();
  public abstract int baseId();
  public abstract int resourceId();
  public abstract int rating();
  public abstract int height();
  public abstract int attribute1();
  public abstract int attribute2();
  public abstract int attribute3();
  public abstract int attribute4();
  public abstract int attribute5();
  public abstract int attribute6();
  public abstract boolean rare();
  public abstract String firstName();
  public abstract String lastName();
  public abstract String commonName();
  public abstract String dateOfBirth();
  public abstract Foot foot();
  public abstract Club club();
  public abstract Nation nation();

  enum Foot {
    Left, Right
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Player.typeAdapterFactory();
  }
}
