package com.elifut.models;

import com.elifut.BuildConfig;
import com.elifut.TestElifutApplication;
import com.elifut.TestFixtures;
import com.elifut.services.ClubDataStore;
import com.elifut.services.ElifutDataStore;

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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ClubTest {
  @Inject ElifutDataStore service;
  @Inject ClubDataStore clubDataStore;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testDefaultStats() {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.stats();
    assertThat(stats.points()).isEqualTo(0);
    assertThat(stats.goals()).isEqualTo(0);
    assertThat(stats.losses()).isEqualTo(0);
    assertThat(stats.wins()).isEqualTo(0);
    assertThat(stats.draws()).isEqualTo(0);
  }

  @Test public void testNewWithWin() {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.newWithWin(2).stats();
    assertThat(stats.points()).isEqualTo(3);
    assertThat(stats.goals()).isEqualTo(2);
    assertThat(stats.losses()).isEqualTo(0);
    assertThat(stats.draws()).isEqualTo(0);
    assertThat(stats.wins()).isEqualTo(1);
  }

  @Test public void testNewWithDraw() {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.newWithDraw().stats();
    assertThat(stats.points()).isEqualTo(1);
    assertThat(stats.goals()).isEqualTo(0);
    assertThat(stats.losses()).isEqualTo(0);
    assertThat(stats.draws()).isEqualTo(1);
    assertThat(stats.wins()).isEqualTo(0);
  }

  @Test public void testNewWithLoss() {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.newWithLoss(-3).stats();
    assertThat(stats.points()).isEqualTo(0);
    assertThat(stats.goals()).isEqualTo(-3);
    assertThat(stats.losses()).isEqualTo(1);
    assertThat(stats.draws()).isEqualTo(0);
    assertThat(stats.wins()).isEqualTo(0);
  }

  @Test public void testSubstitutes() {
    Club club = Club.builder().id(0).name("Sample").build();
    service.create(club);
    Player gornaldo = TestFixtures.GORNALDO.toBuilder().clubId(0).build();
    Player pele = TestFixtures.PELE.toBuilder().clubId(0).build();
    List<Player> players = Arrays.asList(gornaldo, pele);
    ClubSquad clubSquad = ClubSquad.create(0, Collections.singletonList(gornaldo));
    service.create(players);
    service.create(clubSquad);
    List<? extends Player> substitutes = club.substitutes(clubDataStore);
    assertThat(substitutes).isEqualTo(Collections.singletonList(pele));
  }
}