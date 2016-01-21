package com.felipecsl.elifut.models.converter;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.util.ContentValuesBuilder;

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
    return ContentValuesBuilder.create()
        .put("base_id", player.base_id())
        .put("id", player.id())
        .put("club_id", player.clubId())
        .put("first_name", player.first_name())
        .put("last_name", player.last_name())
        .put("name", player.name())
        .put("common_name", player.common_name())
        .put("position", player.position())
        .put("image", player.image())
        .put("nation_image", player.nation_image())
        .put("rating", player.rating())
        .put("player_type", player.player_type())
        .put("attribute_1", player.attribute_1())
        .put("attribute_2", player.attribute_2())
        .put("attribute_3", player.attribute_3())
        .put("attribute_4", player.attribute_4())
        .put("attribute_5", player.attribute_5())
        .put("attribute_6", player.attribute_6())
        .put("quality", player.quality())
        .put("color", player.color())
        .build();
  }
}
