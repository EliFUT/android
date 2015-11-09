package com.felipecsl.elifut;

import android.os.Build;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.models.Club;

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
public class MatchResultsControllerTest {
  private final Club userClub = Club.builder().id(1).name("Club A").build();
  private final Club nonUserClub = Club.builder().id(2).name("Club B").build();
  private final Observable<Club> leagueClubs = Observable.just(userClub, nonUserClub);

  @Inject ElifutPreferences preferences;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);

    preferences.storeUserClub(userClub);
    preferences.storeLeagueClubs(leagueClubs);
  }

  @Test public void testUserWinner() throws Exception {
    MatchResultsController controller = new MatchResultsController(preferences);
    MatchStatistics statistics = new TestMatchStatistics() {
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
    assertThat(preferences.retrieveUserClub()).isEqualTo(newUserClub);
    assertThat(preferences.retrieveLeagueClubs().toList().toBlocking().first())
        .containsOnlyElementsOf(Arrays.asList(newUserClub, nonUserClub.newWithLoss()));
  }

  @Test public void testUserLoss() {
    MatchResultsController controller = new MatchResultsController(preferences);
    MatchStatistics statistics = new TestMatchStatistics() {
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
    assertThat(preferences.retrieveUserClub()).isEqualTo(newUserClub);
    assertThat(preferences.retrieveLeagueClubs().toList().toBlocking().first())
        .containsOnlyElementsOf(Arrays.asList(newUserClub, nonUserClub.newWithWin()));
  }

  @Test public void testDraw() {
    MatchResultsController controller = new MatchResultsController(preferences);
    MatchStatistics statistics = new TestMatchStatistics() {
      @Override public Club home() {
        return userClub;
      }

      @Override public Club away() {
        return nonUserClub;
      }

      @Override public boolean isDraw() {
        return true;
      }
    };

    controller.updateByMatchStatistics(statistics);

    Club newUserClub = userClub.newWithDraw();
    assertThat(preferences.retrieveUserClub()).isEqualTo(newUserClub);
    assertThat(preferences.retrieveLeagueClubs().toList().toBlocking().first())
        .containsOnlyElementsOf(Arrays.asList(newUserClub, nonUserClub.newWithDraw()));
  }
}