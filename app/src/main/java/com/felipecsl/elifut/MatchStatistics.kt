package com.felipecsl.elifut

import com.felipecsl.elifut.models.Club

import org.apache.commons.math3.distribution.NormalDistribution

interface MatchStatistics {
  fun home(): Club
  fun away(): Club
  fun winner(): Club?
  fun loser(): Club?
  fun isDraw(): Boolean
  fun isHomeWin(): Boolean
  fun isAwayWin(): Boolean
  fun homeGoals(): Int
  fun awayGoals(): Int
  fun finalScore(): String
  fun refereeName(): String
  fun homeStatistics(): ClubStatistics
  fun awayStatistics(): ClubStatistics

  data class ClubStatistics(val goals: Int, val shots: Int, val shotsOnTarget: Int, val corners: Int,
      val offsides: Int, val fouls: Int, val yellowCards: Int, val redCards: Int)

  companion object {
    val HOME_WIN_PROBABILITY = .465f
    val DRAW_PROBABILITY = HOME_WIN_PROBABILITY + .174f
    val GOALS_DISTRIBUTION = NormalDistribution(2.6, 1.7)
  }
}
