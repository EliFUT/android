package com.felipecsl.elifut.models.converter;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestFixtures;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutDataStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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