package com.felipecsl.elifut.services;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates a {@link ClubSquad} for the provided team based on the list of all the available
 * players for that team. The returned squad has exactly 11 players, including: 1 GK, 4 defenders, 4
 * midfielders and 2 attackers. It will try to use exactly 1 RB and 1 LB from the provided list. If
 * it can't find them, then other positions (CB) will be used to complete the squad.
 */
public final class ClubSquadBuilder {
  private final Club club;
  private final List<Player> allPlayers;
  private final Comparator<Player> comparator = (c1, c2) -> c2.rating() - c1.rating();

  public ClubSquadBuilder(Club club, List<Player> allPlayers) {
    this.club = club;
    this.allPlayers = allPlayers;
  }

  public ClubSquad build() {
    FluentIterable<Player> defenders = findByAnyPosition(Player.DEFENDER_POSITIONS);
    List<Player> midfielders = new ArrayList<>(
        findByAnyPosition(Player.MIDFIELDER_POSITIONS).toSortedList(comparator));
    List<Player> goalkeepers = new ArrayList<>(playersByPosition("GK").toSortedList(comparator));
    Preconditions.checkNotNull(allPlayers);
    Preconditions.checkArgument(allPlayers.size() >= 11, "Need at least 11 players");
    Preconditions.checkArgument(!goalkeepers.isEmpty(),
        "Need at least one goalkeeper, club=" + club);
    Preconditions.checkArgument(defenders.size() > 3, "Need at least 4 defenders, club=" + club);
    Preconditions.checkArgument(midfielders.size() > 3,
        "Need at least 4 midfielders, club=" + club);

    int totalDefenders = 4;
    int totalMidfielders = 4;
    int totalAttackers = 2;
    List<Player> squad = new ArrayList<>(11);
    List<Player> lbs = new ArrayList<>(
        defenders.filter(positionFilter("LB")).toSortedList(comparator));
    List<Player> cbs = new ArrayList<>(
        defenders.filter(positionFilter("CB")).toSortedList(comparator));
    List<Player> rbs = new ArrayList<>(
        defenders.filter(positionFilter("RB")).toSortedList(comparator));
    List<Player> sortedAttackers = new ArrayList<>(
        findByAnyPosition(Player.ATTACKER_POSITIONS).toSortedList(comparator));

    squad.add(goalkeepers.remove(0));

    if (!lbs.isEmpty()) {
      squad.add(lbs.remove(0));
    } else {
      squad.add(cbs.remove(0));
    }
    // -2 to account for LB and RB who are added separately
    for (int i = 0; i < totalDefenders - 2; i++) {
      squad.add(cbs.remove(0));
    }
    if (!rbs.isEmpty()) {
      squad.add(rbs.remove(0));
    } else {
      squad.add(cbs.remove(0));
    }
    for (int i = 0; i < totalMidfielders; i++) {
      squad.add(midfielders.remove(0));
    }
    if (sortedAttackers.size() >= totalAttackers) {
      for (int i = 0; i < totalAttackers; i++) {
        squad.add(sortedAttackers.remove(0));
      }
    } else {
      while (!midfielders.isEmpty() && squad.size() < 11) {
        squad.add(midfielders.remove(0));
      }
      while (!cbs.isEmpty() && squad.size() < 11) {
        squad.add(cbs.remove(0));
      }
      while (!goalkeepers.isEmpty() && squad.size() < 11) {
        squad.add(goalkeepers.remove(0));
      }
    }

    return ClubSquad.create(club.id(), squad);
  }

  private Predicate<Player> positionFilter(String position) {
    return p -> p.position().equals(position);
  }

  private FluentIterable<Player> findByAnyPosition(List<String> possibilities) {
    return findPlayerBy(p -> possibilities.indexOf(p.position()) != -1);
  }

  private FluentIterable<Player> playersByPosition(String position) {
    return findPlayerBy(positionFilter(position));
  }

  private Player playerByPosition(String position) {
    return playersByPosition(position).first().get();
  }

  private FluentIterable<Player> findPlayerBy(Predicate<Player> predicate) {
    return FluentIterable.from(allPlayers).filter(predicate);
  }
}
