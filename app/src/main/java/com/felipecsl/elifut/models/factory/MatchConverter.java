package com.felipecsl.elifut.models.factory;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Persistable;
import com.felipecsl.elifut.services.ElifutPersistenceService;
import com.felipecsl.elifut.util.ContentValuesBuilder;

import static com.felipecsl.elifut.Util.autoValueTypeFor;
import static com.google.common.base.Preconditions.checkNotNull;

public class MatchConverter extends Persistable.Converter<Match> {
  private final Class<? extends Club> clubType;
  private final Class<? extends MatchResult> matchResultType;

  public MatchConverter() {
    clubType = autoValueTypeFor(Club.class);
    matchResultType = autoValueTypeFor(MatchResult.class);
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
    Club home = checkNotNull(service.query(clubType, cursor.getInt("home_id")));
    Club away = checkNotNull(service.query(clubType, cursor.getInt("away_id")));
    Persistable.Converter<MatchResult> converter = service.converterForType(matchResultType);
    MatchResult matchResult = converter.fromCursor(cursor, service);
    return Match.create(home, away, matchResult);
  }

  /** This assumes that the clubs participating in this match have been previously created. */
  @Override public ContentValues toContentValues(Match match, ElifutPersistenceService service) {
    Persistable.Converter<MatchResult> converter = service.converterForType(matchResultType);
    return ContentValuesBuilder.create()
        .put("home_id", match.home().id())
        .put("away_id", match.away().id())
        .put(converter.toContentValues(match.result(), service))
        .build();
  }
}
