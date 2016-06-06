package com.felipecsl.elifut.models.converter;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ElifutDataStore;

public class PlayerConverter extends Persistable.Converter<Player> {
  @Override public String tableName() {
    return "players";
  }

  @Override public String createStatement() {
    return "CREATE TABLE IF NOT EXISTS "
        + tableName()
        + " ("
        + "id INTEGER PRIMARY KEY, "
        + "base_id INTEGER, "
        + "club_id INTEGER, "
        + "first_name TEXT, "
        + "last_name TEXT, "
        + "name TEXT, "
        + "common_name TEXT, "
        + "position TEXT, "
        + "image TEXT, "
        + "nation_image TEXT, "
        + "rating INTEGER, "
        + "player_type TEXT, "
        + "attribute_1 INTEGER, "
        + "attribute_2 INTEGER, "
        + "attribute_3 INTEGER, "
        + "attribute_4 INTEGER, "
        + "attribute_5 INTEGER, "
        + "attribute_6 INTEGER, "
        + "quality TEXT, "
        + "color TEXT"
        + ")";
  }

  @Override public Player fromCursor(SimpleCursor cursor, ElifutDataStore service) {
    return Player.create(cursor.toCursor());
  }

  @Override public ContentValues toContentValues(Player player, ElifutDataStore service) {
    return player.toContentValues();
  }
}
