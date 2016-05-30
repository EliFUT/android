package com.felipecsl.elifut.models

import com.felipecsl.elifut.util.sort

/** Filters the provided list of players by returns only defenders in that list  */
fun List<Player>.defenders(): List<Player> {
  return filterByPosition(Player.DEFENDER_POSITIONS)
}

/** Filters the provided list of players by returns only midfielders in that list  */
fun List<Player>.midfielders(): List<Player> {
  return filterByPosition(Player.MIDFIELDER_POSITIONS)
}

/** Filters the provided list of players by returns only attackers in that list  */
fun List<Player>.attackers(): List<Player> {
  return filterByPosition(Player.ATTACKER_POSITIONS)
}

fun List<Player>.goalkeepers(): List<Player> {
  return filterByPosition(Player.GOALKEEPER_POSITIONS)
}

private fun List<Player>.filterByPosition(possibilities: List<String>): List<Player> {
  return filter { p -> possibilities.indexOf(p.position()) != -1 }.sort(Player.PLAYER_COMPARATOR)
}