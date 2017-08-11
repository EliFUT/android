package com.elifut.models

import com.elifut.services.ClubDataStore
import com.elifut.util.sample
import com.elifut.util.times
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.random.Well19937c

open class GoalGenerator @JvmOverloads constructor(
    private val dataStore: ClubDataStore,
    private val random: RandomGenerator = Well19937c()) {
  /**
   * Returns {@param qty} goals for the provided {@link Club} where the goal scorer is a random
   * player selected by using the provided {@link RandomGenerator}. By default, attackers have 8x
   * more chances of scoring a goal than midfielders. Midfielders have 4x more chances of scoring
   * a goal than defenders. Goalkeepers can't score goals for now. Ideally this should take into
   * account not only the player position, but his attributes like shooting, for example.
   */
  open fun create(qty: Int, club: Club): List<Goal> {
    return 0.rangeTo(qty - 1).map {
      val squad = dataStore.squad(club)
      val players = squad.players()
      val list = players.attackers().times(players.attackers().size * 8) +
          players.midfielders().times(players.midfielders().size * 4) + players.defenders()
      Goal.create(random.nextInt(90), club, list.sample(random))
    }
  }
}
