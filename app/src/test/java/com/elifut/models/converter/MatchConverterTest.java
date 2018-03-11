package com.elifut.models.converter;

import com.elifut.AutoValueClasses;
import com.elifut.BuildConfig;
import com.elifut.TestElifutApplication;
import com.elifut.TestFixtures;
import com.elifut.models.Club;
import com.elifut.models.Goal;
import com.elifut.models.Match;
import com.elifut.models.MatchResult;
import com.elifut.services.ElifutDataStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;

import static com.elifut.TestFixtures.newGoal;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MatchConverterTest {
  @Inject ElifutDataStore service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testMatches() {
    Goal goal = newGoal(1, TestFixtures.GREMIO);
    MatchResult matchResult = MatchResult.builder()
        .homeGoals(Collections.singletonList(goal))
        .awayGoals(Collections.emptyList())
        .build(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    Match match = Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult);
    service.create(Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL));
    service.create(match);
    assertThat(service.query(AutoValueClasses.MATCH)).isEqualTo(Collections.singletonList(match));
  }

  @Test public void testMissingResult() {
    Club gremio = TestFixtures.GREMIO;
    Club internacional = TestFixtures.INTERNACIONAL;
    Match match = Match.create(gremio, internacional);
    service.create(Arrays.asList(gremio, internacional));
    service.create(match);
    assertThat(service.query(AutoValueClasses.MATCH)).isEqualTo(Collections.singletonList(match));
  }
}