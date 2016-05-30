package com.felipecsl.elifut.models

import com.felipecsl.elifut.TestFixtures
import com.felipecsl.elifut.services.ClubDataStore
import com.felipecsl.elifut.util.times
import org.apache.commons.math3.random.RandomGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GoalGeneratorTest {
  val dataStore = mock(ClubDataStore::class.java)
  val random = mock(RandomGenerator::class.java)

  @Test fun simple() {
    val player = TestFixtures.PELE
    `when`(dataStore.squad(any<Club>())).thenReturn(ClubSquad.create(0, listOf(player)))

    val club = Club.create(3, "Club C")
    val generator = GoalGenerator(dataStore, random)
    assertThat(generator.create(3, club))
        .containsOnly(*listOf(Goal.create(0, club, player)).times(3).toTypedArray())
  }

  @Test fun empty() {
    val club = Club.create(3, "Club C")
    val generator = GoalGenerator(dataStore, random)
    assertThat(generator.create(0, club)).isEqualTo(emptyList<Goal>())
  }
}