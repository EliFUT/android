package com.felipecsl.elifut.services;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestFixtures;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class ElifutPersistenceServiceTest {
  @Inject ElifutPersistenceService service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testQueryEmptyData() {
    assertThat(service.query(AutoValueClasses.CLUB)).isEqualTo(Collections.emptyList());
  }

  @Test public void testQueryById() {
    List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    service.create(clubs);
    assertThat(service.queryOne(AutoValueClasses.CLUB, TestFixtures.GREMIO.id()))
        .isEqualTo(TestFixtures.GREMIO);
    assertThat(service.queryOne(AutoValueClasses.CLUB, TestFixtures.INTERNACIONAL.id()))
        .isEqualTo(TestFixtures.INTERNACIONAL);
  }

  @Test public void testQueryWithWhereClause() {
    // This test method is also testing PlayerConverter indirectly
    List<Player> players = Arrays.asList(TestFixtures.PELE, TestFixtures.GORNALDO);
    service.create(players);
    String clubId = String.valueOf(TestFixtures.PELE.clubId());
    assertThat(service.query(AutoValueClasses.PLAYER, "club_id = ?", clubId))
        .isEqualTo(Collections.singletonList(TestFixtures.PELE));
  }

  @Test public void testObservable() {
    List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    service.create(clubs);
    TestSubscriber<List<? extends Club>> subscriber = new TestSubscriber<>();
    service.observable(AutoValueClasses.CLUB).subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertValue(clubs);
  }

  @Test public void testUpdate() {
    List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    service.create(clubs);
    Club updatedGremio = TestFixtures.GREMIO.toBuilder().small_image("plimba").build();
    assertThat(service.update(updatedGremio, updatedGremio.id())).isEqualTo(1);
    assertThat(service.query(AutoValueClasses.CLUB))
        .isEqualTo(Arrays.asList(updatedGremio, TestFixtures.INTERNACIONAL));
  }

  @Test public void testDelete() {
    List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    service.create(clubs);
    assertThat(service.delete(TestFixtures.GREMIO, TestFixtures.GREMIO.id())).isEqualTo(1);
    assertThat(service.query(AutoValueClasses.CLUB))
        .isEqualTo(Collections.singletonList(TestFixtures.INTERNACIONAL));
  }
}