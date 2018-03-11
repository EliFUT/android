package com.elifut.services;

import com.elifut.AutoValueClasses;
import com.elifut.BuildConfig;
import com.elifut.TestElifutApplication;
import com.elifut.TestFixtures;
import com.elifut.models.Club;
import com.elifut.models.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ElifutDataStoreTest {
  @Inject ElifutDataStore service;

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
    service.observe(AutoValueClasses.CLUB)
        .subscribe(subscriber);
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

  @Test public void testDeleteAll() {
    service.create(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    service.create(TestFixtures.GORNALDO, TestFixtures.PELE);

    service.deleteAll();

    List<? extends Club> clubs = service.query(AutoValueClasses.CLUB);
    List<? extends Player> players = service.query(AutoValueClasses.PLAYER);

    assertThat(clubs).isEmpty();
    assertThat(players).isEmpty();
  }
}