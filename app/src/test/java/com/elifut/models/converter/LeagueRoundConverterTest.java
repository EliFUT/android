package com.elifut.models.converter;

import android.os.Build;

import com.elifut.AutoValueClasses;
import com.elifut.BuildConfig;
import com.elifut.ElifutTestRunner;
import com.elifut.TestElifutApplication;
import com.elifut.TestFixtures;
import com.elifut.models.Goal;
import com.elifut.models.LeagueRound;
import com.elifut.models.Match;
import com.elifut.models.MatchResult;
import com.elifut.services.ElifutDataStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.elifut.TestFixtures.newGoal;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class LeagueRoundConverterTest {
  @Inject ElifutDataStore service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testPersistence() {
    Goal goal = newGoal(1, TestFixtures.GREMIO);
    MatchResult matchResult = MatchResult.builder()
        .homeGoals(Collections.singletonList(goal))
        .awayGoals(Collections.emptyList())
        .build(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);

    LeagueRound round1 = LeagueRound.create(1, Arrays.asList(
        Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult),
        Match.create(TestFixtures.INTERNACIONAL, TestFixtures.GREMIO, matchResult)));

    LeagueRound round2 = LeagueRound.create(1, Arrays.asList(
        Match.create(TestFixtures.INTERNACIONAL, TestFixtures.GREMIO, matchResult),
        Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult)));

    List<LeagueRound> leagueRounds = Arrays.asList(round1, round2);

    service.create(Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL));
    service.create(leagueRounds);
    assertThat(service.query(AutoValueClasses.LEAGUE_ROUND)).isEqualTo(leagueRounds);
  }
}