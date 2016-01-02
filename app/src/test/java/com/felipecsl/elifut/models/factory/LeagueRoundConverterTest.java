package com.felipecsl.elifut.models.factory;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestUtil;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutPersistenceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class LeagueRoundConverterTest {
  @Inject ElifutPersistenceService service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testPersistence() {
    Goal goal = Goal.create(1, TestUtil.GREMIO);
    MatchResult matchResult = MatchResult.builder()
        .homeGoals(Collections.singletonList(goal))
        .awayGoals(Collections.emptyList())
        .build(TestUtil.GREMIO, TestUtil.INTERNACIONAL);

    LeagueRound round1 = LeagueRound.create(1, Arrays.asList(
        Match.create(TestUtil.GREMIO, TestUtil.INTERNACIONAL, matchResult),
        Match.create(TestUtil.INTERNACIONAL, TestUtil.GREMIO, matchResult)));

    LeagueRound round2 = LeagueRound.create(1, Arrays.asList(
        Match.create(TestUtil.INTERNACIONAL, TestUtil.GREMIO, matchResult),
        Match.create(TestUtil.GREMIO, TestUtil.INTERNACIONAL, matchResult)));

    List<LeagueRound> leagueRounds = Arrays.asList(round1, round2);

    service.create(Arrays.asList(TestUtil.GREMIO, TestUtil.INTERNACIONAL));
    service.create(leagueRounds);
    assertThat(service.query(AutoValueClasses.LEAGUE_ROUND)).isEqualTo(leagueRounds);
  }
}