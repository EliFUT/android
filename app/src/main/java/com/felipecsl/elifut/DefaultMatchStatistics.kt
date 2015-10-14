package com.felipecsl.elifut

import com.felipecsl.elifut.models.Club
import org.apache.commons.math3.distribution.RealDistribution
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.random.Well19937c

class DefaultMatchStatistics(private val home: Club, private val away: Club,
    private val winnerGenerator: RandomGenerator = Well19937c(),
    private val goalsDistribution: RealDistribution) : MatchStatistics {
  private val winner: Club?
  private val winnerGoals: Int
  private val loserGoals: Int

  init {
    val result = winnerGenerator.nextFloat()
    winner = if (result <= MatchStatistics.HOME_WIN_PROBABILITY) {
      home
    } else if (result <= MatchStatistics.DRAW_PROBABILITY) {
      null
    } else {
      away
    }
    var totalGoals = Math.round(goalsDistribution.sample()).toInt()
    if (!isDraw()) {
      if (totalGoals <= 2) {
        winnerGoals = totalGoals
        loserGoals = 0
      } else {
        // 3+ goals
        loserGoals = winnerGenerator.nextInt(totalGoals)
        winnerGoals = totalGoals - loserGoals
      }
    } else {
      val evenGoals = if (totalGoals % 2 == 0) totalGoals else totalGoals + 1
      winnerGoals = evenGoals / 2
      loserGoals = evenGoals / 2
    }
  }

  override fun home(): Club {
    return home
  }

  override fun away(): Club {
    return away
  }

  override fun winner(): Club? {
    return winner
  }

  override fun loser(): Club? {
    if (isDraw()) {
      return null
    }
    return if (isHomeWin()) away else home
  }

  override fun isHomeWin(): Boolean {
    if (isDraw()) {
      return false
    }

    return winner!!.equals(home)
  }

  override fun isAwayWin(): Boolean {
    if (isDraw()) {
      return false
    }

    return winner!!.equals(away)
  }

  override fun isDraw(): Boolean {
    return winner == null
  }

  override fun finalScore(): String {
    return "${homeGoals()}x${awayGoals()}"
  }

  override fun homeGoals(): Int {
    return if (isHomeWin()) winnerGoals else loserGoals
  }

  override fun awayGoals(): Int {
    return if (isAwayWin()) winnerGoals else loserGoals
  }

  override fun refereeName(): String {
    return "John Doe"
  }

  override fun homeStatistics(): MatchStatistics.ClubStatistics {
    return MatchStatistics.ClubStatistics(0, 0, 0, 0, 0, 0, 0, 0)
  }

  override fun awayStatistics(): MatchStatistics.ClubStatistics {
    return MatchStatistics.ClubStatistics(0, 0, 0, 0, 0, 0, 0, 0)
  }
}
