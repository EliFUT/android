package com.elifut;

import android.os.Build;
import android.support.annotation.Nullable;

import com.elifut.match.MatchResultController;
import com.elifut.models.Club;
import com.elifut.models.Goal;
import com.elifut.models.MatchResult;
import com.elifut.preferences.UserPreferences;
import com.elifut.services.ElifutDataStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class MatchResultControllerTest {
  private final Club userClub = Club.create(1, "Club A");
  private final Club nonUserClub = Club.create(2, "Club B");
  private final Observable<Club> leagueClubs = Observable.just(userClub, nonUserClub);

  @Inject UserPreferences userPreferences;
  @Inject ElifutDataStore persistenceService;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);

    userPreferences.clubPreference().set(userClub);
    persistenceService.create(leagueClubs.toList().toBlocking().first());
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

      @Override public List<Goal> homeGoals() {
        return Collections.singletonList(mock(Goal.class));
      }

      @Override public List<Goal> awayGoals() {
        return Collections.emptyList();
      }

      @Override public boolean isDraw() {
        return false;
      }
    };
    controller.updateWithResult(matchResult);

    Club newUserClub = userClub.newWithWin(1);
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

      @Override public List<Goal> homeGoals() {
        return Collections.singletonList(mock(Goal.class));
      }

      @Override public List<Goal> awayGoals() {
        return Collections.emptyList();
      }

      @Override public boolean isDraw() {
        return false;
      }
    };

    controller.updateWithResult(matchResult);

    Club newUserClub = userClub.newWithLoss(-1);
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
  }

  @Test public void testDraw() {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = mock(TestMatchResult.class);
    when(matchResult.isDraw()).thenReturn(true);
    when(matchResult.homeGoals()).thenReturn(Collections.emptyList());
    when(matchResult.awayGoals()).thenReturn(Collections.emptyList());

    controller.updateWithResult(matchResult);

    Club newUserClub = userClub.newWithDraw();
    assertThat(userPreferences.clubPreference().get()).isEqualTo(newUserClub);
  }

  @Test public void testCoinsUpdateAfterWin() {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = new TestMatchResult() {
      @Nullable @Override public Club winner() {
        return userClub;
      }

      @Nullable @Override public Club loser() {
        return nonUserClub;
      }

      @Override public List<Goal> homeGoals() {
        return Collections.singletonList(mock(Goal.class));
      }

      @Override public List<Goal> awayGoals() {
        return Collections.emptyList();
      }

      @Override public boolean isDraw() {
        return false;
      }
    };
    Long coinsBefore = userPreferences.coins();
    controller.updateWithResult(matchResult);
    assertThat(userPreferences.coins() - coinsBefore).isEqualTo(UserPreferences.COINS_PRIZE_WIN);
  }

  @Test public void testCoinsUpdateAfterLoss() {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = new TestMatchResult() {
      @Nullable @Override public Club winner() {
        return nonUserClub;
      }

      @Nullable @Override public Club loser() {
        return userClub;
      }

      @Override public List<Goal> homeGoals() {
        return Collections.singletonList(mock(Goal.class));
      }

      @Override public List<Goal> awayGoals() {
        return Collections.emptyList();
      }

      @Override public boolean isDraw() {
        return false;
      }
    };
    Long coinsBefore = userPreferences.coins();
    controller.updateWithResult(matchResult);
    assertThat(userPreferences.coins() - coinsBefore).isEqualTo(UserPreferences.COINS_PRIZE_LOSS);
  }

  @Test public void testCoinsUpdateAfterDraw() {
    MatchResultController controller = new MatchResultController(userPreferences);
    MatchResult matchResult = new TestMatchResult() {
      @Override public boolean isDraw() {
        return true;
      }

      @Override public List<Goal> homeGoals() {
        return Collections.emptyList();
      }

      @Override public List<Goal> awayGoals() {
        return Collections.emptyList();
      }
    };
    Long coinsBefore = userPreferences.coins();
    controller.updateWithResult(matchResult);
    assertThat(userPreferences.coins() - coinsBefore).isEqualTo(UserPreferences.COINS_PRIZE_DRAW);
  }
}