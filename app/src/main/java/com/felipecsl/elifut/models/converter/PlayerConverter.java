package com.felipecsl.elifut.models.converter;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ElifutPersistenceService;
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

  @Override public Player fromCursor(SimpleCursor cursor, ElifutPersistenceService service) {
    return Player.builder()
        .base_id(cursor.getInt("base_id"))
        .id(cursor.getInt("id"))
        .clubId(cursor.getInt("club_id"))
        .first_name(cursor.getString("first_name"))
        .last_name(cursor.getString("last_name"))
        .name(cursor.getString("name"))
        .common_name(cursor.getString("common_name"))
        .position(cursor.getString("position"))
        .image(cursor.getString("image"))
        .nation_image(cursor.getString("nation_image"))
        .rating(cursor.getInt("rating"))
        .player_type(cursor.getString("player_type"))
        .attribute_1(cursor.getInt("attribute_1"))
        .attribute_2(cursor.getInt("attribute_2"))
        .attribute_3(cursor.getInt("attribute_3"))
        .attribute_4(cursor.getInt("attribute_4"))
        .attribute_5(cursor.getInt("attribute_5"))
        .attribute_6(cursor.getInt("attribute_6"))
        .quality(cursor.getString("quality"))
        .color(cursor.getString("color"))
        .build();
  }

  @Override public ContentValues toContentValues(Player player, ElifutPersistenceService service) {
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
