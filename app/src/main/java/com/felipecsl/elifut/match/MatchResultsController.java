package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.JsonPreference;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;

import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.toList;

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
    allClubs = Observable.from(clubsPreference.get());
  }

  /** Updates the league and clubs based on the provided match results */
  public void updateByMatchStatistics(MatchResult statistics) {
    if (!statistics.isDraw()) {
      Club winner = statistics.winner();
      Club loser = statistics.loser();

      if (userClub.nameEquals(winner)) {
        // user is winner
        Club winnerClub = userClub.newWithWin();
        updateUserClub(winnerClub);
        clubsPreference.set(toList(allClubs.compose(transform(winnerClub, loser.newWithLoss()))));
      } else {
        // computer is winner
        Club loserClub = userClub.newWithLoss();
        updateUserClub(loserClub);
        clubsPreference.set(toList(allClubs.compose(transform(winner.newWithWin(), loserClub))));
      }
    } else {
      // match result is draw
      Club nonUserClub = userClub.nameEquals(statistics.match().home())
          ? statistics.match().away() : statistics.match().home();
      Club drawClub = userClub.newWithDraw();
      updateUserClub(drawClub);
      clubsPreference.set(toList(allClubs.compose(transform(drawClub, nonUserClub.newWithDraw()))));
    }
  }

  private void updateUserClub(Club updated) {
    JsonPreference<Club> userClubPreference = userPreferences.clubPreference();
    userClubPreference.set(updated);
  }

  private Observable.Transformer<Club, Club> transform(Club clubA, Club clubB) {
    return (Observable<Club> observable) -> allClubs
        .filter((club) -> !club.nameEquals(clubB))
        .filter((club) -> !club.nameEquals(clubA))
        .mergeWith(Observable.just(clubA))
        .mergeWith(Observable.just(clubB));
  }
}
