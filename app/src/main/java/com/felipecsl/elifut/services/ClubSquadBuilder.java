package com.felipecsl.elifut.services;

import com.google.common.base.Preconditions;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Player;
import com.felipecsl.elifut.models.PlayersKt;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Generates a {@link ClubSquad} for the provided team based on the list of all the available
 * players for that team. The returned squad has exactly 11 players, including: 1 GK, 4 defenders, 4
 * midfielders and 2 attackers.
 */
public final class ClubSquadBuilder {
  private final Club club;
  private final List<Player> squad = new ArrayList<>(11);
  private final List<Player> defenders;
  private final List<Player> midfielders;
  private final List<Player> goalkeepers;
  private final List<Player> attackers;

  public ClubSquadBuilder(Club club, List<Player> allPlayers) {
    Preconditions.checkNotNull(allPlayers);
    Preconditions.checkArgument(allPlayers.size() >= 11, "Need at least 11 players, club=" + club);
    this.club = club;
    defenders = new ArrayList<>(PlayersKt.defenders(allPlayers));
    midfielders = new ArrayList<>(PlayersKt.midfielders(allPlayers));
    goalkeepers = new ArrayList<>(PlayersKt.goalkeepers(allPlayers));
    attackers = new ArrayList<>(PlayersKt.attackers(allPlayers));
    Preconditions.checkArgument(!goalkeepers.isEmpty(), "Need at least 1 goalkeeper, club=" + club);
  }

  public ClubSquad build() {
    squad.add(goalkeepers.remove(0));

    addToSquad(ClubSquad.TOTAL_DEFENDERS, asList(defenders, midfielders, attackers, goalkeepers));
    addToSquad(ClubSquad.TOTAL_MIDFIELDERS, asList(midfielders, attackers, defenders, goalkeepers));
    addToSquad(ClubSquad.TOTAL_ATTACKERS, asList(attackers, midfielders, defenders, goalkeepers));

    return ClubSquad.create(club.id(), squad);
  }

  private void addToSquad(int quantity, List<List<Player>> from) {
    int i = 0;
    while (squad.size() < 11 && quantity > 0) {
      if (from.get(i).isEmpty()) {
        i++;
        continue;
      }
      squad.add(from.get(i).remove(0));
      quantity--;
    }
  }
}
