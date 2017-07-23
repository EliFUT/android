package com.elifut.preferences;

import android.os.Build;

import com.elifut.AutoValueClasses;
import com.elifut.BuildConfig;
import com.elifut.ElifutTestRunner;
import com.elifut.TestElifutApplication;
import com.elifut.TestFixtures;
import com.elifut.match.MatchResultGenerator;
import com.elifut.models.Club;
import com.elifut.models.ClubSquad;
import com.elifut.models.Goal;
import com.elifut.models.LeagueRound;
import com.elifut.models.Match;
import com.elifut.models.MatchResult;
import com.elifut.services.ClubDataStore;
import com.elifut.services.ElifutDataStore;
import com.elifut.services.LeagueRoundGenerator;

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

import static com.elifut.TestFixtures.newGoal;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class LeagueDetailsTest {
  private final List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
  private final Goal goal = newGoal(1, TestFixtures.GREMIO);
  private final MatchResult matchResult = MatchResult.builder()
      .homeGoals(Collections.singletonList(goal))
      .awayGoals(emptyList())
      .build(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
  private final LeagueRound round1 = LeagueRound.create(1, 1, Arrays.asList(
      Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult),
      Match.create(TestFixtures.INTERNACIONAL, TestFixtures.GREMIO, matchResult)));
  private final LeagueRound round2 = LeagueRound.create(2, 1, Arrays.asList(
      Match.create(TestFixtures.INTERNACIONAL, TestFixtures.GREMIO, matchResult),
      Match.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL, matchResult)));
  private final List<LeagueRound> rounds = Arrays.asList(round1, round2);
  private final Club gremioWithWin = TestFixtures.GREMIO.newWithWin(2);
  private final Club flamengoWithWin = TestFixtures.FLAMENGO.newWithWin(1);
  private final Club internacionalWithLoss = TestFixtures.INTERNACIONAL.newWithLoss(-2);
  private final Club fluminenseWithLoss = TestFixtures.FLUMINENSE.newWithLoss(-1);
  private LeagueDetails leagueDetails;

  @Inject ElifutDataStore persistenceService;
  @Inject MatchResultGenerator matchResultGenerator;
  @Mock ClubDataStore clubDataStore;
  @Mock LeagueRoundGenerator roundGenerator;
  @Mock ElifutDataStore mockPersistenceStore;

  @Before public void setUp() throws Exception {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
    initMocks(this);

    leagueDetails = new LeagueDetails(persistenceService, clubDataStore, roundGenerator,
        matchResultGenerator);
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
    assertThat(leagueDetails.rounds()).isEqualTo(emptyList());
  }

  @Test public void testExecuteRound() {
    Club gremio = TestFixtures.GREMIO;
    Club internacional = TestFixtures.INTERNACIONAL;
    MatchResultGenerator mockResultGenerator = mock(MatchResultGenerator.class);
    MatchResult fakeResult = MatchResult.builder()
        .homeGoals(Collections.singletonList(newGoal(10, gremio)))
        .awayGoals(emptyList())
        .build(gremio, internacional);

    when(clubDataStore.squad(any())).thenReturn(ClubSquad.create(0, emptyList()));
    ClubSquad homeSquad = ClubSquad.create(0, emptyList());
    ClubSquad awaySquad = ClubSquad.create(0, emptyList());
    when(mockResultGenerator.generate(gremio, homeSquad, internacional, awaySquad))
        .thenReturn(fakeResult);

    LeagueDetails leagueDetails = new LeagueDetails(
        persistenceService, clubDataStore, roundGenerator, mockResultGenerator);
    Match match = Match.create(gremio, internacional);
    LeagueRound round = LeagueRound.create(1, Collections.singletonList(match));

    LeagueRound leagueRound = leagueDetails.executeRound(round);

    verify(mockResultGenerator).generate(gremio, homeSquad, internacional, awaySquad);
    assertThat(leagueRound.matches().get(0).result()).isEqualTo(fakeResult);
  }

  @Test public void clubsStandings() {
    when(mockPersistenceStore.query(AutoValueClasses.CLUB)).thenReturn(
        Arrays.asList(gremioWithWin, internacionalWithLoss, flamengoWithWin, fluminenseWithLoss));

    LeagueDetails leagueDetails = new LeagueDetails(mockPersistenceStore, clubDataStore,
        roundGenerator, matchResultGenerator);

    List<? extends Club> standings = leagueDetails.clubsStandings();

    // Expected standings:
    // Gremio - 3 pts, 2 goals
    // Flamengo - 3 pts, 1 goal
    // Fluminense - 0 pts, -1 goals
    // Internacional - 0 pts, -2 goals
    assertThat(standings).isEqualTo(Arrays.asList(
        gremioWithWin, flamengoWithWin, fluminenseWithLoss, internacionalWithLoss));
  }

  @Test public void clubPosition() {
    when(mockPersistenceStore.query(AutoValueClasses.CLUB)).thenReturn(
        Arrays.asList(gremioWithWin, internacionalWithLoss, flamengoWithWin, fluminenseWithLoss));

    LeagueDetails leagueDetails = new LeagueDetails(mockPersistenceStore, clubDataStore,
        roundGenerator, matchResultGenerator);

    assertThat(leagueDetails.clubPosition(gremioWithWin)).isEqualTo(0);
    assertThat(leagueDetails.clubPosition(flamengoWithWin)).isEqualTo(1);
    assertThat(leagueDetails.clubPosition(fluminenseWithLoss)).isEqualTo(2);
    assertThat(leagueDetails.clubPosition(internacionalWithLoss)).isEqualTo(3);
  }
}