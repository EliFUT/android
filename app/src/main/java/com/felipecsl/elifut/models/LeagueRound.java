package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class LeagueRound {
  public abstract int roundNumber();
  public abstract List<Match> matches();

  public static LeagueRound create(int roundNumber, List<Match> matches) {
    return new AutoValue_LeagueRound(roundNumber, matches);
  }

  public static LeagueRound create(int roundNumber) {
    return new AutoValue_LeagueRound(roundNumber, new ArrayList<>());
  }

  public void addMatch(Match match) {
    matches().add(match);
  }

  /** Returns {@code true} if any of the provided clubs are playing on this round */
  public boolean anyClubPlaying(Club... clubs) {
    for (Match match : matches()) {
      for (Club club : clubs) {
        if (match.hasClub(club)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Round " + roundNumber() + ": " + "\n");
    for (Match match : matches()) {
      stringBuilder.append(match).append("\n");
    }
    return stringBuilder.toString();
  }
}
