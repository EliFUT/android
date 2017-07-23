package com.elifut.models.converter;

import android.content.ContentValues;

import com.elifut.AutoValueClasses;
import com.elifut.SimpleCursor;
import com.elifut.models.Club;
import com.elifut.models.Match;
import com.elifut.models.MatchResult;
import com.elifut.models.Persistable;
import com.elifut.services.ElifutDataStore;
import com.elifut.util.ContentValuesBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class MatchConverter extends Persistable.Converter<Match> {
  public MatchConverter() {
  }

  @Override public String tableName() {
    return "matches";
  }

  @Override public String createStatement() {
    return "CREATE TABLE IF NOT EXISTS "
        + tableName()
        + " ("
        + "id INTEGER PRIMARY KEY, "
        + "home_id INTEGER, "
        + "away_id INTEGER, "
        + "result BLOB"
        + ")";
  }

  @Override public Match fromCursor(SimpleCursor cursor, ElifutDataStore service) {
    Club home = checkNotNull(service.queryOne(AutoValueClasses.CLUB, cursor.getInt("home_id")));
    Club away = checkNotNull(service.queryOne(AutoValueClasses.CLUB, cursor.getInt("away_id")));
    Persistable.Converter<MatchResult> converter =
        service.converterForType(AutoValueClasses.MATCH_RESULT);
    MatchResult matchResult = converter.fromCursor(cursor, service);
    return Match.create(cursor.getInt("id"), home, away, matchResult);
  }

  /** This assumes that the clubs participating in this match have been previously created. */
  @Override public ContentValues toContentValues(Match match, ElifutDataStore service) {
    Persistable.Converter<MatchResult> converter =
        service.converterForType(AutoValueClasses.MATCH_RESULT);
    ContentValuesBuilder contentValuesBuilder = ContentValuesBuilder.create()
        .put("home_id", match.home().id())
        .put("away_id", match.away().id());

    if (match.result() != null) {
      contentValuesBuilder.put(converter.toContentValues(match.result(), service));
    }

    return contentValuesBuilder.build();
  }
}
