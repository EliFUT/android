package com.felipecsl.elifut;

import android.os.Build;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.match.MatchResultController;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class MatchResultControllerTest {
  private final Club userClub = Club.create(1, "Club A");
  private final Club nonUserClub = Club.create(2, "Club B");
  private final Observable<Club> leagueClubs = Observable.just(userClub, nonUserClub);

  @Inject UserPreferences userPreferences;
  @Inject LeaguePreferences leaguePreferences;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);

    userPreferences.clubPreference().set(userClub);
    leaguePreferences.clubsPreference().set(leagueClubs.toList().toBlocking().first());
  }

  @Test public void testUserWinner() throws Exception {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = new TestMatchResult() {
      @Nullable @Override public Club winner() {
        return userClub;
      }

      @Nullable @Override public Club loser() {
        return nonUserClub;
      }

      @Override public boolean isDraw() {
        return false;
      }
    };
    controller.updateWithResult(matchResult);

    Club newUserClub = userClub.newWithWin();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
  }

  @Test public void testUserLoss() {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = new TestMatchResult() {
      @Nullable @Override public Club winner() {
        return nonUserClub;
      }

      @Nullable @Override public Club loser() {
        return userClub;
      }

      @Override public boolean isDraw() {
        return false;
      }
    };

    controller.updateWithResult(matchResult);

    Club newUserClub = userClub.newWithLoss();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
  }

  @Test public void testDraw() {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = new TestMatchResult() {
      @Override public boolean isDraw() {
        return true;
      }
    };

    controller.updateWithResult(matchResult);

    Club newUserClub = userClub.newWithDraw();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
  }
}