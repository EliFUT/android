package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
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

  public void execute(Observable<Match> matches) {
    clubsPreference.set(toList(matches.flatMap(this::filterClubsByResult)));
  }

  private Observable<Club> filterClubsByResult(Match match) {
    MatchResult matchResult = match.result();
    //noinspection ConstantConditions
    return !matchResult.isDraw()
        ? Observable.just(matchResult.loser().newWithLoss(), matchResult.winner().newWithWin())
        : Observable.just(match.home().newWithDraw(), match.away().newWithDraw());
  }
}
