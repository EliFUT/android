package com.elifut.services

import android.support.annotation.VisibleForTesting
import android.util.Log
import android.util.SparseArray
import com.elifut.models.Club
import com.elifut.models.LeagueRound
import com.elifut.models.Match
import com.elifut.util.shuffle
import com.google.common.base.Preconditions
import java.util.*

open class LeagueRoundGenerator {
  @VisibleForTesting fun generateRoundsDeterministic(clubs: List<Club>): List<LeagueRound> {
    Preconditions.checkNotNull(clubs)
    Preconditions.checkArgument(clubs.size > 1, "Need at least 2 clubs")

    val clubsCopy = ArrayList(clubs)

    if (clubsCopy.size % 2 != 0) {
      clubsCopy.removeAt(clubsCopy.size - 1)
      Log.d(TAG, "Got odd number of clubs, dropping the last one")
    }

    val totalClubs = clubsCopy.size
    val totalRounds = (totalClubs - 1) * 2
    val matchesPerRound = totalClubs / 2
    val rounds = arrayOfNulls<LeagueRound>(totalRounds)

    for (k in rounds.indices) {
      rounds[k] = LeagueRound.create(k + 1)
    }
    val clubMap = mutableMapOf<Int, Club>()
    for (i in 1..totalClubs) {
      clubMap.put(i, clubsCopy[i - 1])
    }

    for (round in 0..totalRounds - 1) {
      for (match in 0..matchesPerRound - 1) {
        val home = (round + match) % (totalClubs - 1)
        var away = (totalClubs - 1 - match + round) % (totalClubs - 1)

        // Last team stays in the same place while the others
        // rotate around it.
        if (match == 0) {
          away = totalClubs - 1
        }

        val clubHome: Club
        val clubAway: Club
        if (round % 2 == 0) {
          clubHome = clubMap[home + 1]!!
          clubAway = clubMap[away + 1]!!
        } else {
          clubHome = clubMap[away + 1]!!
          clubAway = clubMap[home + 1]!!
        }

        val newMatch = Match.create(clubHome, clubAway)
        rounds[round]!!.addMatch(newMatch)
      }
    }

    return Arrays.asList<LeagueRound>(*rounds)
  }

  /** Generates a random list of league rounds from the provided list of clubs.  */
  open fun generateRounds(clubs: List<Club>): List<LeagueRound> {
    // Make a defensive copy since we need to mutate it
    return generateRoundsDeterministic(ArrayList(clubs).shuffle())
  }

  companion object {
    private val TAG = "LeagueRoundGenerator"
  }
}
