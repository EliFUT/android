package com.felipecsl.elifut;

import android.os.Build;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.match.MatchResultsController;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.preferences.LeaguePreferences;
import com.felipecsl.elifut.preferences.UserPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MatchResultControllerTest {
  private final Club userClub = Club.builder().id(1).name("Club A").build();
  private final Club nonUserClub = Club.builder().id(2).name("Club B").build();
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
    MatchResultsController controller =
        new MatchResultsController(userPreferences, leaguePreferences);
    MatchResult statistics = new TestMatchResult() {
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
    controller.updateByMatchStatistics(statistics);

    Club newUserClub = userClub.newWithWin();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
    assertThat(leaguePreferences.clubsPreference().get())
        .containsOnlyElementsOf(Arrays.asList(newUserClub, nonUserClub.newWithLoss()));
  }

  @Test public void testUserLoss() {
    MatchResultsController controller =
        new MatchResultsController(userPreferences, leaguePreferences);
    MatchResult statistics = new TestMatchResult() {
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

    controller.updateByMatchStatistics(statistics);

    Club newUserClub = userClub.newWithLoss();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
    assertThat(leaguePreferences.clubsPreference().get())
        .containsOnlyElementsOf(Arrays.asList(newUserClub, nonUserClub.newWithWin()));
  }

  @Test public void testDraw() {
    MatchResultsController controller =
        new MatchResultsController(userPreferences, leaguePreferences);
    MatchResult statistics = new TestMatchResult() {
      @Override public Match match() {
        return Match.create(userClub, nonUserClub);
      }

      @Override public boolean isDraw() {
        return true;
      }
    };

    controller.updateByMatchStatistics(statistics);

    Club newUserClub = userClub.newWithDraw();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
    assertThat(leaguePreferences.clubsPreference().get())
        .containsOnlyElementsOf(Arrays.asList(newUserClub, nonUserClub.newWithDraw()));
  }
}