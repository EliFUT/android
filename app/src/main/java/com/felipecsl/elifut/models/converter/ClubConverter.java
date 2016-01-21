package com.felipecsl.elifut.models.converter;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubStats;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.util.ContentValuesBuilder;

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
    ClubStats stats = club.nonNullStats();
    return ContentValuesBuilder.create()
        .put("id", club.id())
        .put("base_id", club.base_id())
        .put("name", club.name())
        .put("abbrev_name", club.abbrev_name())
        .put("small_image", club.small_image())
        .put("large_image", club.large_image())
        .put("league_id", club.league_id())
        .put("points", stats.points())
        .put("wins", stats.wins())
        .put("draws", stats.draws())
        .put("losses", stats.losses())
        .put("goals", stats.goals())
        .build();
  }
}
