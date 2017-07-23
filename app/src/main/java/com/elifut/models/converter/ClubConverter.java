package com.elifut.models.converter;

import android.content.ContentValues;

import com.elifut.SimpleCursor;
import com.elifut.models.Club;
import com.elifut.models.Persistable;
import com.elifut.services.ElifutDataStore;

public class ClubConverter extends Persistable.Converter<Club> {
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

  @Override public Club fromCursor(SimpleCursor cursor,
      ElifutDataStore elifutDataStore) {
    return Club.create(cursor.toCursor());
  }

  @Override public ContentValues toContentValues(Club club, ElifutDataStore service) {
    return club.toContentValues();
  }
}
