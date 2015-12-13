package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.JsonPreference;
import com.felipecsl.elifut.preferences.LeaguePreferences;

import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.toList;

/**
 * Executes a league round by generating results for each match in that round and updating each
 * club stats with their respective results (win/draw/loss).
 */
final class LeagueRoundExecutor {
  private final JsonPreference<List<Club>> clubsPreference;
  private final MatchResultGenerator generator;

  LeagueRoundExecutor(LeaguePreferences leaguePreferences, MatchResultGenerator generator) {
    this.generator = generator;
    clubsPreference = leaguePreferences.clubsPreference();
  }

  void execute(LeagueRound round) {
    List<Club> updatedClubs = toList(Observable.from(round.matches())
        .map(generator::generate)
        .flatMap(this::filterClubsByResult));

    clubsPreference.set(updatedClubs);
  }

  private Observable<Club> filterClubsByResult(MatchResult result) {
    return !result.isDraw()
        ? Observable.just(result.loser().newWithLoss(), result.winner().newWithWin())
        : Observable.just(result.home().newWithDraw(), result.away().newWithDraw());
  }
}
