package com.felipecsl.elifut.models;

final class League {
  private final String name;
  private final Nation nation;

  League(String name, Nation nation) {
    this.name = name;
    this.nation = nation;
  }

  public static League fromId(int id) {
    return null;
  }
}
