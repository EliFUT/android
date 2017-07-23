package com.elifut.match

import android.util.Log
import com.elifut.models.*
import org.apache.commons.math3.distribution.RealDistribution
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.random.Well19937c
import java.util.*

/** Generates random results for matches  */
open class MatchResultGenerator @JvmOverloads constructor(
    private val goalGenerator: GoalGenerator,
    private val random: RandomGenerator = Well19937c(),
    private val goalsDistribution: RealDistribution = MatchResult.GOALS_DISTRIBUTION) {

  open fun generate(home: Club, homeSquad: ClubSquad, away: Club, awaySquad: ClubSquad): MatchResult {
    val result = random.nextFloat()
    val matchResult = MatchResult.builder()
    val winnerGoals: List<Goal>
    val loserGoals: List<Goal>

    val ratingDifference = homeSquad.rating() - awaySquad.rating()
    val ratingDiffModifier = ratingDifference * 0.7 / 100
    val homeWinProbability = MatchResult.HOME_WIN_PROBABILITY + ratingDiffModifier
    val drawProbability = homeWinProbability + MatchResult.DRAW_PROBABILITY

    val winner = if (result <= homeWinProbability) {
      home
    } else if (result <= drawProbability) {
      null
    } else {
      away
    }

    Log.d(TAG, String.format(Locale.getDefault(),
        "%s X %s\nHome winning Odds=%.2f, Away winning odds=%.2f\nWinner=%s, Result=%.2f",
        home, away, homeWinProbability * 100, (1 - drawProbability) * 100,
        winner, result * 100))

    val isHomeWin = home == winner
    val loser = if (isHomeWin) away else home
    val totalGoals = Math.max(Math.floor(goalsDistribution.sample()).toInt(), 0)

    if (winner != null) {
      if (totalGoals <= 2) {
        // 1x0 or 2x0
        winnerGoals = goalGenerator.create(Math.max(totalGoals, 1), winner)
        loserGoals = emptyList<Goal>()
      } else {
        // 3+ goals (eg.: 3x1, 3x0, 4x0, etc)
        loserGoals = goalGenerator.create(random.nextInt(Math.max(1, totalGoals / 2 + 1)), loser)
        winnerGoals = goalGenerator.create(totalGoals - loserGoals.size, winner)
      }
    } else {
      // draw (0x0, 1x1, 2x2, etc)
      val evenGoals = if (totalGoals % 2 == 0) totalGoals else totalGoals + 1
      winnerGoals = goalGenerator.create(evenGoals / 2, home)
      loserGoals = goalGenerator.create(evenGoals / 2, away)
    }

    val homeGoals = if (isHomeWin) winnerGoals else loserGoals
    val awayGoals = if (isHomeWin) loserGoals else winnerGoals

    return matchResult.homeGoals(homeGoals).awayGoals(awayGoals).build(home, away)
  }

  companion object {
    private val TAG = MatchResultGenerator::class.java.simpleName
  }
}
