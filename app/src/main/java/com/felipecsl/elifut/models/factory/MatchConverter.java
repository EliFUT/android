package com.felipecsl.elifut.models.factory;

import android.content.ContentValues;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.services.ElifutPersistenceService;
import com.felipecsl.elifut.util.ContentValuesBuilder;

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

  @Override public Match fromCursor(SimpleCursor cursor, ElifutPersistenceService service) {
    Club home = checkNotNull(service.queryOne(AutoValueClasses.CLUB, cursor.getInt("home_id")));
    Club away = checkNotNull(service.queryOne(AutoValueClasses.CLUB, cursor.getInt("away_id")));
    Persistable.Converter<MatchResult> converter =
        service.converterForType(AutoValueClasses.MATCH_RESULT);
    MatchResult matchResult = converter.fromCursor(cursor, service);
    return Match.create(cursor.getInt("id"), home, away, matchResult);
  }

  /** This assumes that the clubs participating in this match have been previously created. */
  @Override public ContentValues toContentValues(Match match, ElifutPersistenceService service) {
    Persistable.Converter<MatchResult> converter =
        service.converterForType(AutoValueClasses.MATCH_RESULT);
    return ContentValuesBuilder.create()
        .put("home_id", match.home().id())
        .put("away_id", match.away().id())
        .put(converter.toContentValues(match.result(), service))
        .build();
  }
}
