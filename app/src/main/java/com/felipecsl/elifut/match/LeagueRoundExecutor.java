package com.felipecsl.elifut.match;

import com.google.common.collect.FluentIterable;

import android.util.Log;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutDataStore;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/** Executes a league round by updating each club stats with their respective win, draw or loss. */
public final class LeagueRoundExecutor {
  private static final String TAG = "LeagueRoundExecutor";
  private final ElifutDataStore persistenceService;

  public LeagueRoundExecutor(ElifutDataStore persistenceService) {
    this.persistenceService = persistenceService;
  }

  public void execute(List<Match> matches) {
    for (Club club : FluentIterable.from(matches).transformAndConcat(this::filterClubsByResult)) {
      Log.d(TAG, "Updating club " + club.abbrev_name() + " with stats " + club.stats());
      persistenceService.update(club, club.id());
    }
  }

  private Iterable<? extends Club> filterClubsByResult(Match match) {
    MatchResult matchResult = checkNotNull(match.result(), "Match result can't be null");
    int goalsDiff = Math.abs(matchResult.homeGoals().size() - matchResult.awayGoals().size());
    Club home = match.home();
    Club away = match.away();

    if (matchResult.isDraw()) {
      return Arrays.asList(home.newWithDraw(), away.newWithDraw());
    } else if (checkNotNull(matchResult.loser()).nameEquals(home)) {
      // GOTCHA: We can't directly use matchResult.loser() and winner() here to update the stats
      // because those object don't really have the most up-to-date stats for each club. That
      // happens because we serialize MatchResult as a JSON to the database and that doesn't get to
      // reflect the changes to clubs on the end of each round. We should consider fixing that by
      // not storing MatchResult as a JSON but, instead, normalizing it as a regular object on the
      // database.
      return Arrays.asList(home.newWithLoss(-goalsDiff), away.newWithWin(goalsDiff));
    } else {
      return Arrays.asList(away.newWithLoss(-goalsDiff), home.newWithWin(goalsDiff));
    }
  }
}
