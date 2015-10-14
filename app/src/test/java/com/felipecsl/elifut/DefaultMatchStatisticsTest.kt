package com.felipecsl.elifut

import com.felipecsl.elifut.models.Club
import org.apache.commons.math3.distribution.RealDistribution
import org.apache.commons.math3.random.RandomGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DefaultMatchStatisticsTest {
  private var home: Club = Club.create(0, "Gremio", "", "", 0)
  private var away: Club = Club.create(1, "Internacional", "", "", 0)
  private var fakeNumberGenerator: RandomGenerator = mock(RandomGenerator::class.java)
  private var goalsDistribution: RealDistribution = mock(RealDistribution::class.java)

  @Test fun testSimpleHomeWin() {
    `when`(fakeNumberGenerator.nextFloat()).thenReturn(MatchStatistics.HOME_WIN_PROBABILITY)
    `when`(goalsDistribution.sample()).thenReturn(1.0)

    val stats = DefaultMatchStatistics(home, away, fakeNumberGenerator, goalsDistribution)

    assertThat(stats.home()).isEqualTo(home)
    assertThat(stats.away()).isEqualTo(away)
    assertThat(stats.winner()).isEqualTo(home)
    assertThat(stats.loser()).isEqualTo(away)
    assertThat(stats.finalScore()).isEqualTo("1x0")
    assertThat(stats.isDraw()).isEqualTo(false)
    assertThat(stats.homeGoals()).isEqualTo(1)
    assertThat(stats.awayGoals()).isEqualTo(0)
  }

  @Test fun testSimpleDraw() {
    `when`(fakeNumberGenerator.nextFloat()).thenReturn(MatchStatistics.DRAW_PROBABILITY)
    `when`(goalsDistribution.sample()).thenReturn(2.0)

    val stats = DefaultMatchStatistics(home, away, fakeNumberGenerator, goalsDistribution)

    assertThat(stats.home()).isEqualTo(home)
    assertThat(stats.away()).isEqualTo(away)
    assertThat(stats.winner()).isEqualTo(null)
    assertThat(stats.loser()).isEqualTo(null)
    assertThat(stats.finalScore()).isEqualTo("1x1")
    assertThat(stats.isDraw()).isEqualTo(true)
    assertThat(stats.homeGoals()).isEqualTo(1)
    assertThat(stats.awayGoals()).isEqualTo(1)
  }

  @Test fun testSimpleAwayWin() {
    `when`(fakeNumberGenerator.nextFloat()).thenReturn(MatchStatistics.DRAW_PROBABILITY + 0.1f)
    `when`(goalsDistribution.sample()).thenReturn(1.0)

    val stats = DefaultMatchStatistics(home, away, fakeNumberGenerator, goalsDistribution)

    assertThat(stats.home()).isEqualTo(home)
    assertThat(stats.away()).isEqualTo(away)
    assertThat(stats.winner()).isEqualTo(away)
    assertThat(stats.loser()).isEqualTo(home)
    assertThat(stats.finalScore()).isEqualTo("0x1")
    assertThat(stats.isDraw()).isEqualTo(false)
    assertThat(stats.homeGoals()).isEqualTo(0)
    assertThat(stats.awayGoals()).isEqualTo(1)
  }
}
