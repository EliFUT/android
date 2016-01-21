package com.felipecsl.elifut.services;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.Player;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates a {@link ClubSquad} for the provided team based on the list of all the available
 * players for that team. The returned squad has exactly 11 players, including: 1 GK, 4 defenders,
 * 4 midfielders and 2 attackers. It will try to use exactly 1 RB and 1 LB from the provided list.
 * If it can't find them, then other positions (CB) will be used to complete the squad.
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
    Preconditions.checkNotNull(allPlayers);
    Preconditions.checkArgument(allPlayers.size() >= 11, "Need at least 11 players");
    Preconditions.checkArgument(!playersByPosition("GK").isEmpty(), "Need at least one goalkeeper");
    Preconditions.checkArgument(!playersByPosition("GK").isEmpty(), "Need at least one goalkeeper");
    FluentIterable<Player> defenders = findByAnyPosition(Player.DEFENDER_POSITIONS);
    Preconditions.checkArgument(defenders.size() > 3, "Need at least 4 defenders");
    FluentIterable<Player> midfielders = findByAnyPosition(Player.MIDFIELDER_POSITIONS);
    Preconditions.checkArgument(midfielders.size() > 3, "Need at least 4 midfielders");
    FluentIterable<Player> attackers = findByAnyPosition(Player.ATTACKER_POSITIONS);
    Preconditions.checkArgument(attackers.size() > 1, "Need at least 2 attackers");

    List<Player> squad = new ArrayList<>(11);
    squad.add(playerByPosition("GK"));
    List<Player> lbs = new ArrayList<>(
        defenders.filter(positionFilter("LB")).toSortedList(comparator));
    List<Player> cbs = new ArrayList<>(
        defenders.filter(positionFilter("CB")).toSortedList(comparator));
    List<Player> rbs = new ArrayList<>(
        defenders.filter(positionFilter("RB")).toSortedList(comparator));
    if (!lbs.isEmpty()) {
      squad.add(lbs.get(0));
    } else {
      squad.add(cbs.remove(0));
    }
    squad.add(cbs.remove(0));
    squad.add(cbs.remove(0));
    if (!rbs.isEmpty()) {
      squad.add(rbs.get(0));
    } else {
      squad.add(cbs.remove(0));
    }
    List<Player> sortedMidfielders = midfielders.toSortedList(comparator);
    List<Player> sortedAttackers = attackers.toSortedList(comparator);
    squad.add(sortedMidfielders.get(0));
    squad.add(sortedMidfielders.get(1));
    squad.add(sortedMidfielders.get(2));
    squad.add(sortedMidfielders.get(3));
    squad.add(sortedAttackers.get(0));
    squad.add(sortedAttackers.get(1));

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
