package com.felipecsl.elifut.models.factory;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.services.ElifutPersistenceService;
import com.felipecsl.elifut.util.ContentValuesBuilder;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public final class MatchResultConverter extends Persistable.Converter<MatchResult> {
  private final JsonAdapter<MatchResult> adapter;

  public MatchResultConverter(Moshi moshi) {
    adapter = moshi.adapter(MatchResult.class);
  }

  @Override public String tableName() {
    return "match_results";
  }

  @Override public String createStatement() {
    // This is unused since MatchResult is serialized as a JSON blob
    return "CREATE TABLE IF NOT EXISTS "
        + tableName()
        + " ("
        + "id INTEGER PRIMARY KEY"
        + ")";
  }

  @Override public MatchResult fromCursor(SimpleCursor cursor, ElifutPersistenceService service) {
    try {
      return adapter.fromJson(cursor.getString("result"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override public ContentValues toContentValues(
      MatchResult matchResult, ElifutPersistenceService service) {
    return ContentValuesBuilder.create()
        .put("result", adapter.toJson(matchResult))
        .build();
  }
}
