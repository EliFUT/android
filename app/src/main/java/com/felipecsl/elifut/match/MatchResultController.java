package com.felipecsl.elifut.match;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.JsonPreference;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;

import java.util.List;

import rx.Observable;

import static com.felipecsl.elifut.util.CollectionUtils.toList;

public final class MatchResultController {
  private final UserPreferences userPreferences;
  private final Club userClub;
  private final Observable<Club> allClubs;
  private final JsonPreference<List<Club>> clubsPreference;

  public MatchResultController(
      UserPreferences userPreferences, LeaguePreferences leaguePreferences) {
    this.userPreferences = userPreferences;
    userClub = userPreferences.clubPreference().get();
    clubsPreference = leaguePreferences.clubsPreference();
    // Could use clubsPreference.asObservable().flatMap(Observable::from); below, but
    // that doesnt work because, since asObservable() never calls onCompleted(), calling toList()
    // on it hangs forever.
    allClubs = Observable.from(clubsPreference.get());
  }

  /** Updates the league and clubs based on the provided match results */
  public void updateWithResult(MatchResult result) {
    if (!result.isDraw()) {
      Club winner = result.winner();
      Club loser = result.loser();

      if (userClub.nameEquals(winner)) {
        // user is winner
        Club winnerClub = userClub.newWithWin();
        updateUserClub(winnerClub);
        updateClubs(winnerClub, loser.newWithLoss());
      } else {
        // computer is winner
        Club loserClub = userClub.newWithLoss();
        updateUserClub(loserClub);
        updateClubs(winner.newWithWin(), loserClub);
      }
    } else {
      // match result is draw
      Match match = result.match();
      Club nonUserClub = userClub.nameEquals(match.home()) ? match.away() : match.home();
      Club drawClub = userClub.newWithDraw();
      updateUserClub(drawClub);
      updateClubs(drawClub, nonUserClub.newWithDraw());
    }
  }

  private void updateUserClub(Club updated) {
    JsonPreference<Club> userClubPreference = userPreferences.clubPreference();
    userClubPreference.set(updated);
  }

  private void updateClubs(Club clubA, Club clubB) {
    clubsPreference.set(toList(allClubs.compose(transform(clubA, clubB))));
  }

  private Observable.Transformer<Club, Club> transform(Club clubA, Club clubB) {
    return (Observable<Club> observable) -> allClubs
        .filter((club) -> !club.nameEquals(clubB))
        .filter((club) -> !club.nameEquals(clubA))
        .mergeWith(Observable.just(clubA))
        .mergeWith(Observable.just(clubB));
  }
}
