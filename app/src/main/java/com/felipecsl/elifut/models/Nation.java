package com.felipecsl.elifut.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Nation extends Model {
  @JsonProperty private final int id;
  @JsonProperty private final int baseId;
  @JsonProperty private final String name;

  Nation(int id, int baseId, String name) {
    this.id = id;
    this.baseId = baseId;
    this.name = name;
  }

  Nation() {
    this(0, 0, null);
  }

  public String image() {
    return null;
  }

  public int id() {
    return id;
  }

  public static Nation fromId(int id) {
    return null;
  }

  @Override public String toString() {
    return name;
  }
}
