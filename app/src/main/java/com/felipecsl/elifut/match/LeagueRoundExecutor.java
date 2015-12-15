package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.JsonPreference;
import com.felipecsl.elifut.preferences.LeaguePreferences;

import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.toList;

/** Executes a league round by updating each club stats with their respective win, draw or loss. */
public final class LeagueRoundExecutor {
  private final JsonPreference<List<Club>> clubsPreference;

  public LeagueRoundExecutor(LeaguePreferences leaguePreferences) {
    clubsPreference = leaguePreferences.clubsPreference();
  }

  public void execute(Observable<MatchResult> results) {
    clubsPreference.set(toList(results.flatMap(this::filterClubsByResult)));
  }

  private Observable<Club> filterClubsByResult(MatchResult result) {
    return !result.isDraw()
        ? Observable.just(result.loser().newWithLoss(), result.winner().newWithWin())
        : Observable.just(result.home().newWithDraw(), result.away().newWithDraw());
  }
}
