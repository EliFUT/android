package com.felipecsl.elifut.services;

import android.os.Build;

import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubFactory;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class ElifutPersistenceServiceTest {
  private final ClubFactory clubFactory = new ClubFactory();
  private final ElifutPersistenceService service = new ElifutPersistenceService(
      RuntimeEnvironment.application, SqlBrite.create(),
      Collections.singletonList(clubFactory));

  @Test public void testCreateClubs() {
    Club gremio = Club.builder()
        .name("Gremio")
        .small_image("abc")
        .large_image("def")
        .league_id(1)
        .id(1)
        .base_id(1)
        .build();
    Club internacional = Club.builder()
        .name("Internacional")
        .small_image("xxx")
        .large_image("xyz")
        .league_id(4)
        .id(2)
        .base_id(3)
        .build();
    List<Club> clubs = Arrays.asList(gremio, internacional);
    service.create(clubs);
    assertThat(service.query(clubFactory.targetType())).isEqualTo(clubs);
  }

  @Test public void testQueryEmptyData() {
    assertThat(service.query(clubFactory.targetType())).isEqualTo(Collections.emptyList());
  }
}