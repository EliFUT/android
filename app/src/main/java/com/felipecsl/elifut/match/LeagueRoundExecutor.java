package com.felipecsl.elifut.match;

import android.util.Log;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutPersistenceService;
import com.google.common.collect.FluentIterable;

import java.util.Arrays;
import java.util.List;

/** Executes a league round by updating each club stats with their respective win, draw or loss. */
public final class LeagueRoundExecutor {
  private static final String TAG = "LeagueRoundExecutor";
  private final ElifutPersistenceService persistenceService;

  public LeagueRoundExecutor(ElifutPersistenceService persistenceService) {
    this.persistenceService = persistenceService;
  }

  public void execute(List<Match> matches) {
    for (Club club : FluentIterable.from(matches).transformAndConcat(this::filterClubsByResult)) {
      Log.d(TAG, "Updating club " + club.abbrev_name() + " with stats " + club.stats());
      persistenceService.update(club, club.id());
    }
  }

  private Iterable<? extends Club> filterClubsByResult(Match match) {
    MatchResult matchResult = match.result();
    if (matchResult.isDraw()) {
      return Arrays.asList(match.home().newWithDraw(), match.away().newWithDraw());
    }
    //noinspection ConstantConditions
    if (matchResult.loser().nameEquals(match.home())) {
      // GOTCHA: We can't directly use matchResult.loser() and winner() here to update the stats
      // because those object don't really have the most up-to-date stats for each club. That
      // happens because we serialize MatchResult as a JSON to the database and that doesn't get to
      // reflect the changes to clubs on the end of each round. We should consider fixing that by
      // not storing MatchResult as a JSON but, instead, normalizing it as a regular object on the
      // database.
      return Arrays.asList(match.home().newWithLoss(), match.away().newWithWin());
    }
    return Arrays.asList(match.away().newWithLoss(), match.home().newWithWin());
  }
}
