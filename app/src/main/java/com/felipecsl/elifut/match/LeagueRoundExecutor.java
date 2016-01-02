package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutPersistenceService;

import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.toList;

/** Executes a league round by updating each club stats with their respective win, draw or loss. */
public final class LeagueRoundExecutor {
  private final ElifutPersistenceService persistenceService;

  public LeagueRoundExecutor(ElifutPersistenceService persistenceService) {
    this.persistenceService = persistenceService;
  }

  public void execute(List<Match> matches) {
    List<Club> clubs = toList(Observable.from(matches).flatMap(this::filterClubsByResult));
    for (Club club : clubs) {
      persistenceService.update(club, club.id());
    }
  }

  private Observable<Club> filterClubsByResult(Match match) {
    MatchResult matchResult = match.result();
    //noinspection ConstantConditions
    return !matchResult.isDraw()
        ? Observable.just(matchResult.loser().newWithLoss(), matchResult.winner().newWithWin())
        : Observable.just(match.home().newWithDraw(), match.away().newWithDraw());
  }
}
