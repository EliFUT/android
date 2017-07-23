package com.elifut.models.converter;

import android.content.ContentValues;

import com.elifut.SimpleCursor;
import com.elifut.models.ClubSquad;
import com.elifut.models.Persistable;
import com.elifut.services.ElifutDataStore;

public class ClubSquadConverter extends Persistable.Converter<ClubSquad> {
  @Override public String tableName() {
    return "club_squads";
  }

  @Override public String createStatement() {
    return "CREATE TABLE IF NOT EXISTS "
        + tableName()
        + " ("
        + "id INTEGER PRIMARY KEY, "
        + "club_id INTEGER, "
        + "player_ids TEXT"
        + ")";
  }

  @Override public ClubSquad fromCursor(SimpleCursor cursor, ElifutDataStore service) {
    return ClubSquad.create(cursor, service);
  }

  @Override public ContentValues toContentValues(ClubSquad clubSquad, ElifutDataStore service) {
    return clubSquad.toContentValues();
  }
}
