package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;

import rx.Observable;

public final class MatchResultsController {
  private final UserPreferences userPreferences;
  private final LeaguePreferences leaguePreferences;
  private final Club userClub;
  private final Observable<Club> allClubs;

  public MatchResultsController(
      UserPreferences userPreferences, LeaguePreferences leaguePreferences) {
    this.userPreferences = userPreferences;
    this.leaguePreferences = leaguePreferences;
    userClub = userPreferences.getUserClub();
    allClubs = leaguePreferences.getLeagueClubs();
  }

  public void updateByMatchStatistics(MatchStatistics statistics) {
    if (!statistics.isDraw()) {
      Club winner = statistics.winner();

      if (userClub.nameEquals(winner)) {
        // user is winner
        Club winnerClub = userClub.newWithWin();
        userPreferences.putUserClub(winnerClub);
        Observable<Club> observable = allClubs.compose(
            transform(winnerClub, statistics.loser().newWithLoss()));
        leaguePreferences.putLeagueClubs(observable);
      } else {
        // computer is winner
        Club loserClub = userClub.newWithLoss();
        userPreferences.putUserClub(loserClub);
        Observable<Club> observable = allClubs.compose(
            transform(statistics.winner().newWithWin(), loserClub));
        leaguePreferences.putLeagueClubs(observable);
      }
    } else {
      // match result is draw
      Club nonUserClub = userClub.nameEquals(statistics.home())
          ? statistics.away() : statistics.home();
      Club drawClub = userClub.newWithDraw();
      userPreferences.putUserClub(drawClub);
      Observable<Club> observable = allClubs.compose(
          transform(drawClub, nonUserClub.newWithDraw()));
      leaguePreferences.putLeagueClubs(observable);
    }
  }

  private Observable.Transformer<Club, Club> transform(Club clubA, Club clubB) {
    return (Observable<Club> observable) -> allClubs
        .filter((club) -> !club.nameEquals(clubB))
        .filter((club) -> !club.nameEquals(clubA))
        .mergeWith(Observable.just(clubA))
        .mergeWith(Observable.just(clubB));
  }
}
