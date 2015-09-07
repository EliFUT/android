package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class League extends Model {
  public abstract String name();
  public abstract Nation nation();
}
