package com.felipecsl.elifut.models;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClubTest {
  @Test public void testDefaultStats() throws Exception {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.stats();
    assertThat(stats.points()).isEqualTo(0);
    assertThat(stats.goals()).isEqualTo(0);
    assertThat(stats.losses()).isEqualTo(0);
    assertThat(stats.wins()).isEqualTo(0);
    assertThat(stats.draws()).isEqualTo(0);
  }

  @Test public void testNewWithWin() throws Exception {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.newWithWin().stats();
    assertThat(stats.points()).isEqualTo(3);
    assertThat(stats.goals()).isEqualTo(0);
    assertThat(stats.losses()).isEqualTo(0);
    assertThat(stats.draws()).isEqualTo(0);
    assertThat(stats.wins()).isEqualTo(1);
  }

  @Test public void testNewWithDraw() throws Exception {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.newWithDraw().stats();
    assertThat(stats.points()).isEqualTo(1);
    assertThat(stats.goals()).isEqualTo(0);
    assertThat(stats.losses()).isEqualTo(0);
    assertThat(stats.draws()).isEqualTo(1);
    assertThat(stats.wins()).isEqualTo(0);
  }

  @Test public void testNewWithLoss() throws Exception {
    Club club = Club.builder().id(0).name("Sample").build();
    ClubStats stats = club.newWithLoss().stats();
    assertThat(stats.points()).isEqualTo(0);
    assertThat(stats.goals()).isEqualTo(0);
    assertThat(stats.losses()).isEqualTo(1);
    assertThat(stats.draws()).isEqualTo(0);
    assertThat(stats.wins()).isEqualTo(0);
  }
}