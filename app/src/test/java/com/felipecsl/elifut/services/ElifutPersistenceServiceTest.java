package com.felipecsl.elifut.services;

import android.os.Build;

import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.factory.ClubConverter;
import com.felipecsl.elifut.models.factory.MatchConverter;
import com.felipecsl.elifut.models.factory.MatchResultConverter;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class ElifutPersistenceServiceTest {
  private final ClubConverter clubConverter = new ClubConverter();
  private final MatchConverter matchConverter = new MatchConverter();
  private final MatchResultConverter matchResultConverter = new MatchResultConverter();
  private final List<MatchResult.Converter<?>> converters =
      Arrays.asList(clubConverter, matchConverter, matchResultConverter);
  private final Club gremio = Club.builder()
      .name("Gremio")
      .small_image("abc")
      .large_image("def")
      .league_id(1)
      .id(1)
      .base_id(1)
      .build();
  private final Club inter = Club.builder()
      .name("Internacional")
      .small_image("xxx")
      .large_image("xyz")
      .league_id(4)
      .id(2)
      .base_id(3)
      .build();
  private final ElifutPersistenceService service = new ElifutPersistenceService(
      RuntimeEnvironment.application, SqlBrite.create(), converters);

  @Test public void testCreateClubs() {
    List<Club> clubs = Arrays.asList(gremio, inter);
    service.create(clubs);
    assertThat(service.query(clubConverter.targetType())).isEqualTo(clubs);
  }

  @Test public void testQueryEmptyData() {
    assertThat(service.query(clubConverter.targetType())).isEqualTo(Collections.emptyList());
  }

  @Test public void testQueryById() {
    List<Club> clubs = Arrays.asList(gremio, inter);
    Class<Club> type = clubConverter.targetType();
    service.create(clubs);
    assertThat(service.query(type, gremio.id())).isEqualTo(gremio);
    assertThat(service.query(type, inter.id())).isEqualTo(inter);
  }

  @Test public void testListen() {
    List<Club> clubs = Arrays.asList(gremio, inter);
    service.create(clubs);
    TestSubscriber<Club> subscriber = new TestSubscriber<>();
    service.observable(clubConverter.targetType()).subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertValues(gremio, inter);
  }

  @Test public void testMatches() {
    Goal goal = Goal.create(1, gremio);
    MatchResult matchResult = MatchResult.builder()
        .homeGoals(Collections.singletonList(goal))
        .awayGoals(Collections.emptyList())
        .build(gremio, inter);
    List<Match> matches = Collections.singletonList(Match.create(gremio, inter, matchResult));
    service.create(Arrays.asList(gremio, inter));
    service.create(matches);
    assertThat(service.query(matchConverter.targetType())).isEqualTo(matches);
  }
}