package com.elifut.match;

import com.f2prateek.rx.preferences.Preference;
import com.elifut.models.Club;
import com.elifut.models.MatchResult;
import com.elifut.preferences.JsonPreference;
import com.elifut.preferences.UserPreferences;

public final class MatchResultController {
  private final UserPreferences userPreferences;
  private final Club userClub;

  public MatchResultController(UserPreferences userPreferences) {
    this.userPreferences = userPreferences;
    userClub = userPreferences.club();
  }

  /** Updates the current user club based on the provided match result */
  public void updateWithResult(MatchResult result) {
    JsonPreference<Club> userClubPref = userPreferences.clubPreference();
    Preference<Long> userCoinsPref = userPreferences.coinsPreference();
    int goalDiff = Math.abs(result.homeGoals().size() - result.awayGoals().size());
    if (!result.isDraw()) {
      if (userClub.nameEquals(result.winner())) {
        // user is winner
        userClubPref.set(userClub.newWithWin(goalDiff));
        userCoinsPref.set(userPreferences.coins() + UserPreferences.COINS_PRIZE_WIN);
      } else {
        // computer is winner
        userClubPref.set(userClub.newWithLoss(-goalDiff));
      }
    } else {
      // match result is draw
      userClubPref.set(userClub.newWithDraw());
      userCoinsPref.set(userPreferences.coins() + UserPreferences.COINS_PRIZE_DRAW);
    }
  }
}
