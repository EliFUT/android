package com.felipecsl.elifut.models.converter;

import android.content.ContentValues;
import android.text.TextUtils;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.util.ContentValuesBuilder;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;

import static com.felipecsl.elifut.Util.listSupertype;

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
    List<Integer> playerIds = Lists.transform(Arrays.asList(
        cursor.getString("player_ids").split(",")), Integer::valueOf);
    List<? extends Player> players =
        service.query(AutoValueClasses.PLAYER, Ints.toArray(playerIds));
    return ClubSquad.create(cursor.getInt("id"), cursor.getInt("club_id"), listSupertype(players));
  }

  @Override public ContentValues toContentValues(ClubSquad clubSquad, ElifutDataStore service) {
    return ContentValuesBuilder.create()
        .put("id", clubSquad.id())
        .put("club_id", clubSquad.clubId())
        .put("player_ids", TextUtils.join(",", Lists.transform(clubSquad.players(), Player::id)))
        .build();
  }
}
