package com.felipecsl.elifut.models.converter;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.services.ElifutDataStore;
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

  @Override public MatchResult fromCursor(SimpleCursor cursor, ElifutDataStore service) {
    try {
      String result = cursor.getString("result");
      if (result != null) {
        return adapter.fromJson(result);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override public ContentValues toContentValues(
      MatchResult matchResult, ElifutDataStore service) {
    return ContentValuesBuilder.create()
        .put("result", adapter.toJson(matchResult))
        .build();
  }
}
