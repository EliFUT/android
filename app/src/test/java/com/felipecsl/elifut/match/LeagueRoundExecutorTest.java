package com.felipecsl.elifut.match;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.Util;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutDataStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.felipecsl.elifut.TestFixtures.newGoal;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class LeagueRoundExecutorTest {
  private final Club clubA = Club.create(1, "Club A").newWithWin();
  private final Club clubB = Club.create(2, "Club B").newWithLoss();
  private final Club clubC = Club.create(3, "Club C");
  private final Club clubD = Club.create(4, "Club D");
  private final List<Club> leagueClubs = Arrays.asList(clubA, clubB, clubC, clubD);

  @Inject ElifutDataStore persistenceService;
  @Mock MatchResultGenerator generator;

  private LeagueRoundExecutor executor;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
    MockitoAnnotations.initMocks(this);
    persistenceService.create(leagueClubs);
    executor = new LeagueRoundExecutor(persistenceService);
  }

  @Test public void testExecute() throws Exception {
    MatchResult result1 = MatchResult.builder()
        .awayGoals(Collections.emptyList())
        .homeGoals(Collections.singletonList(newGoal(10, clubA)))
        .build(clubA, clubB);

    MatchResult result2 = MatchResult.builder()
        .homeGoals(Collections.emptyList())
        .awayGoals(Arrays.asList(newGoal(30, clubD), newGoal(40, clubD)))
        .build(clubC, clubD);

    Match match1 = Match.create(clubA, clubB, result1);
    Match match2 = Match.create(clubC, clubD, result2);

    executor.execute(Arrays.asList(match1, match2));

    List<? extends Club> clubs = persistenceService.query(AutoValueClasses.CLUB);
    List<Club> query = Util.listSupertype(clubs);
    assertThat(query).containsOnly(clubA.newWithWin(), clubB.newWithLoss(), clubC.newWithLoss(),
        clubD.newWithWin());
  }

  @Test public void testExecuteDraw() {
    MatchResult result1 = MatchResult.builder()
        .homeGoals(Collections.singletonList(newGoal(10, clubA)))
        .awayGoals(Collections.singletonList(newGoal(15, clubB)))
        .build(clubA, clubB);

    MatchResult result2 = MatchResult.builder()
        .homeGoals(Collections.emptyList())
        .awayGoals(Collections.emptyList())
        .build(clubC, clubD);

    Match match1 = Match.create(clubA, clubB, result1);
    Match match2 = Match.create(clubC, clubD, result2);

    executor.execute(Arrays.asList(match1, match2));

    List<? extends Club> clubs = persistenceService.query(AutoValueClasses.CLUB);
    List<Club> query = Util.listSupertype(clubs);
    assertThat(query).containsOnly(clubA.newWithDraw(), clubB.newWithDraw(), clubC.newWithDraw(),
        clubD.newWithDraw());
  }
}