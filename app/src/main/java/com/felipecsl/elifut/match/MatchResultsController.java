package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.preferences.JsonPreference;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;

import java.util.List;

import rx.Observable;

public final class MatchResultsController {
  private final UserPreferences userPreferences;
  private final Club userClub;
  private final Observable<Club> allClubs;
  private final JsonPreference<List<Club>> clubsPreference;

  public MatchResultsController(
      UserPreferences userPreferences, LeaguePreferences leaguePreferences) {
    this.userPreferences = userPreferences;
    userClub = userPreferences.clubPreference().get();
    clubsPreference = leaguePreferences.clubsPreference();
    allClubs = leaguePreferences.clubsObservable().flatMap(Observable::from);
  }

  public void updateByMatchStatistics(MatchStatistics statistics) {
    if (!statistics.isDraw()) {
      Club winner = statistics.winner();

      if (userClub.nameEquals(winner)) {
        // user is winner
        Club winnerClub = userClub.newWithWin();
        userPreferences.clubPreference().set(winnerClub);
        Observable<Club> observable = allClubs.compose(
            transform(winnerClub, statistics.loser().newWithLoss()));
        clubsPreference.set(toList(observable));
      } else {
        // computer is winner
        Club loserClub = userClub.newWithLoss();
        userPreferences.clubPreference().set(loserClub);
        Observable<Club> observable = allClubs.compose(
            transform(statistics.winner().newWithWin(), loserClub));
        clubsPreference.set(toList(observable));
      }
    } else {
      // match result is draw
      Club nonUserClub = userClub.nameEquals(statistics.home())
          ? statistics.away() : statistics.home();
      Club drawClub = userClub.newWithDraw();
      userPreferences.clubPreference().set(drawClub);
      Observable<Club> observable = allClubs.compose(
          transform(drawClub, nonUserClub.newWithDraw()));
      clubsPreference.set(toList(observable));
    }
  }

  private List<Club> toList(Observable<Club> observable) {
    return observable.toList().toBlocking().first();
  }

  private Observable.Transformer<Club, Club> transform(Club clubA, Club clubB) {
    return (Observable<Club> observable) -> allClubs
        .filter((club) -> !club.nameEquals(clubB))
        .filter((club) -> !club.nameEquals(clubA))
        .mergeWith(Observable.just(clubA))
        .mergeWith(Observable.just(clubB));
  }
}
