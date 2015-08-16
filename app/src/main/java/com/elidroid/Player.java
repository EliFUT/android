package com.elidroid;


final class Player {
  private final int id;
  private final int baseId;
  private final int resourceId;
  private final String firstName;
  private final String lastName;
  private final String commonName;
  private final int rating;
  private final boolean rare;
  private final Foot foot;
  private final int height;
  private final String dateOfBirth;
  private final Club club;
  private final Nation nation;
  private final League league;
  private final int attribute1;
  private final int attribute2;
  private final int attribute3;
  private final int attribute4;
  private final int attribute5;
  private final int attribute6;

  Player(int id, int baseId, int resourceId, String firstName, String lastName, String commonName,
      int rating, boolean rare, Foot foot, int height, String dateOfBirth, Club club, Nation nation,
      League league, int attribute1, int attribute2, int attribute3, int attribute4, int attribute5,
      int attribute6) {
    this.id = id;
    this.baseId = baseId;
    this.resourceId = resourceId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.commonName = commonName;
    this.rating = rating;
    this.rare = rare;
    this.foot = foot;
    this.height = height;
    this.dateOfBirth = dateOfBirth;
    this.club = club;
    this.nation = nation;
    this.league = league;
    this.attribute1 = attribute1;
    this.attribute2 = attribute2;
    this.attribute3 = attribute3;
    this.attribute4 = attribute4;
    this.attribute5 = attribute5;
    this.attribute6 = attribute6;
  }

  enum Foot {
    Left, Right
  }
}
