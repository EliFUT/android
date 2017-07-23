package com.elifut.models.converter;

import android.content.ContentValues;
import android.text.TextUtils;

import com.elifut.AutoValueClasses;
import com.elifut.SimpleCursor;
import com.elifut.models.LeagueRound;
import com.elifut.models.Match;
import com.elifut.models.Persistable;
import com.elifut.services.ElifutDataStore;
import com.elifut.util.ContentValuesBuilder;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;

import static com.elifut.Util.listSupertype;

public class LeagueRoundConverter extends Persistable.Converter<LeagueRound> {
  @Override public String tableName() {
    return "league_rounds";
  }

  @Override public String createStatement() {
    return "CREATE TABLE IF NOT EXISTS "
        + tableName()
        + " ("
        + "id INTEGER PRIMARY KEY, "
        + "round_number INTEGER, "
        + "matches TEXT"
        + ")";
  }

  @Override public LeagueRound fromCursor(SimpleCursor cursor, ElifutDataStore service) {
    List<Integer> matchIds = Lists.transform(Arrays.asList(
        cursor.getString("matches").split(",")), Integer::valueOf);
    List<? extends Match> matches = service.query(AutoValueClasses.MATCH, Ints.toArray(matchIds));
    return LeagueRound.create(cursor.getInt("id"), cursor.getInt("round_number"),
        listSupertype(matches));
  }

  /** First creates a record for each of the matches in this round before creating the round. */
  @Override public ContentValues toContentValues(LeagueRound round, ElifutDataStore service) {
    return ContentValuesBuilder.create()
        .put("round_number", round.roundNumber())
        .put("matches", TextUtils.join(",", Lists.transform(round.matches(), service::create)))
        .build();
  }
}
