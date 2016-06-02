package com.felipecsl.elifut.models;

import com.felipecsl.elifut.TestFixtures;
import com.felipecsl.elifut.services.ClubDataStore;
import com.felipecsl.elifut.util.CollectionUtilsKt;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoalGeneratorTest {
  private final ClubDataStore dataStore = mock(ClubDataStore.class);
  private final RandomGenerator random = mock(RandomGenerator.class);

  @Test public void simple() {
    Player player = TestFixtures.PELE;
    when(dataStore.squad(any())).thenReturn(ClubSquad.create(0, singletonList(player)));

    Club club = Club.create(3, "Club C");
    GoalGenerator generator = new GoalGenerator(dataStore, random);
    List<Goal> goals = singletonList(Goal.create(0, club, player));
    assertThat(generator.create(3, club)).containsOnly(
        CollectionUtilsKt.times(goals, 3).toArray(new Goal[1]));
  }

  @Test public void empty() {
    Club club = Club.create(3, "Club C");
    GoalGenerator generator = new GoalGenerator(dataStore, random);
    assertThat(generator.create(0, club)).isEqualTo(emptyList());
  }
}