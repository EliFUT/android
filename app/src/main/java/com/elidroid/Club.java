package com.elidroid;

import java.util.List;

final class Club {
  private final String name;
  private final List<Player> players;
  private final League league;

  Club(String name, List<Player> players, League league) {
    this.name = name;
    this.players = players;
    this.league = league;
  }

  public String image() {
    return null;
  }

  public static Club fromId(int anInt) {
    return null;
  }
}
