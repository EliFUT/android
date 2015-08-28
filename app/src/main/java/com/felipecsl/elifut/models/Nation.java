package com.felipecsl.elifut.models;

public final class Nation extends Model {
  private final int id;
  private final int base_id;
  private final String name;
  private final String image;

  Nation(int id, int base_id, String name, String image) {
    this.id = id;
    this.base_id = base_id;
    this.name = name;
    this.image = image;
  }

  Nation() {
    this(0, 0, null, null);
  }

  public String image() {
    return image;
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
