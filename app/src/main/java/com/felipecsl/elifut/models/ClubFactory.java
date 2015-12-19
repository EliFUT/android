package com.felipecsl.elifut.models;

import android.database.Cursor;

import com.felipecsl.elifut.SimpleCursor;

public class ClubFactory extends Persistable.Factory<Club> {
  @Override public String tableName() {
    return "clubs";
  }

  @Override public String createStatement() {
    return "CREATE TABLE IF NOT EXISTS "
        + tableName()
        + " ("
        + "id INTEGER PRIMARY KEY, "
        + "base_id INTEGER, "
        + "stat_id INTEGER, "
        + "league_id INTEGER, "
        + "name TEXT, "
        + "points INTEGER, "
        + "wins INTEGER, "
        + "draws INTEGER, "
        + "losses INTEGER, "
        + "goals INTEGER, "
        + "small_image TEXT, "
        + "large_image TEXT, "
        + "abbrev_name TEXT"
        + ")";
  }

  @Override public Club fromCursor(Cursor cursor) {
    SimpleCursor simpleCursor = new SimpleCursor(cursor);
    return Club.builder()
        .id(simpleCursor.getInt("id"))
        .base_id(simpleCursor.getInt("base_id"))
        .stats(clubStatsFromCursor(simpleCursor))
        .league_id(simpleCursor.getInt("league_id"))
        .name(simpleCursor.getString("name"))
        .small_image(simpleCursor.getString("small_image"))
        .large_image(simpleCursor.getString("large_image"))
        .abbrev_name(simpleCursor.getString("abbrev_name"))
        .build();
  }

  private ClubStats clubStatsFromCursor(SimpleCursor cursor) {
    return ClubStats.builder()
        .points(cursor.getInt("points"))
        .wins(cursor.getInt("wins"))
        .draws(cursor.getInt("draws"))
        .losses(cursor.getInt("losses"))
        .goals(cursor.getInt("goals"))
        .build();
  }
}
