package com.felipecsl.elifut.models.converter;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestFixtures;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
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

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class MatchConverterTest {
  @Inject ElifutDataStore service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testMatches() {
    Goal goal = Goal.create(1, TestFixtures.GREMIO);
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