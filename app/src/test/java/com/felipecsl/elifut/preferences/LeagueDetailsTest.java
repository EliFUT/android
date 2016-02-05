package com.felipecsl.elifut.preferences;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestFixtures;
import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.felipecsl.elifut.services.LeagueRoundGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class LeagueDetailsTest {
  private final List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
  private final Goal goal = Goal.create(1, TestFixtures.GREMIO);
  private final MatchResult matchResult = MatchResult.builder()
      .homeGoals(Collections.singletonList(goal))
      .awayGoals(Collections.emptyList())
      .build(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
  private final LeagueRound round1 = LeagueRound.create(1, 1, Arrays.asList(
      Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult),
      Match.create(TestFixtures.INTERNACIONAL, TestFixtures.GREMIO, matchResult)));
  private final LeagueRound round2 = LeagueRound.create(2, 1, Arrays.asList(
      Match.create(TestFixtures.INTERNACIONAL, TestFixtures.GREMIO, matchResult),
      Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult)));
  private final List<LeagueRound> rounds = Arrays.asList(round1, round2);
  private LeagueDetails leagueDetails;

  @Inject ElifutDataStore persistenceService;
  @Inject MatchResultGenerator matchResultGenerator;
  @Mock LeagueRoundGenerator roundGenerator;

  @Before public void setUp() throws Exception {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
    initMocks(this);

    leagueDetails = new LeagueDetails(persistenceService, roundGenerator, matchResultGenerator);
  }

  @Test public void testInitialize() {
    when(roundGenerator.generateRounds(clubs)).thenReturn(rounds);

    leagueDetails.initialize(clubs);

    assertThat(persistenceService.query(AutoValueClasses.CLUB)).isEqualTo(clubs);
    assertThat(leagueDetails.rounds()).isEqualTo(rounds);
  }

  @Test public void testNextRound() {
    when(roundGenerator.generateRounds(clubs)).thenReturn(rounds);

    leagueDetails.initialize(clubs);

    assertThat(leagueDetails.nextRound()).isEqualTo(round1);
    assertThat(leagueDetails.rounds()).isEqualTo(Collections.singletonList(round2));
    assertThat(leagueDetails.nextRound()).isEqualTo(round2);
    assertThat(leagueDetails.rounds()).isEqualTo(Collections.emptyList());
  }

  @Test public void testExecuteRound() {
    Club gremio = TestFixtures.GREMIO;
    Club internacional = TestFixtures.INTERNACIONAL;
    MatchResultGenerator mockResultGenerator = mock(MatchResultGenerator.class);
    MatchResult fakeResult = MatchResult.builder()
        .homeGoals(Collections.singletonList(Goal.create(10, gremio)))
        .awayGoals(Collections.emptyList())
        .build(gremio, internacional);
    when(mockResultGenerator.generate(gremio, internacional))
        .thenReturn(fakeResult);
    LeagueDetails leagueDetails = new LeagueDetails(
        persistenceService, roundGenerator, mockResultGenerator);
    Match match = Match.create(gremio, internacional);
    LeagueRound round = LeagueRound.create(1, Collections.singletonList(match));

    assertThat(leagueDetails.executeRound(round).matches().get(0).result()).isEqualTo(fakeResult);
  }
}