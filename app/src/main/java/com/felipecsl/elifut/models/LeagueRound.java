package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

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

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_LeagueRound.typeAdapterFactory();
  }

  /** Returns the first match in this round that has the provided club or null if none found */
  public Match findMatchByClub(Club club) {
    for (Match match : matches()) {
      if (match.hasClub(club)) {
        return match;
      }
    }
    return null;
  }

  @Override public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Round " + roundNumber() + ": " + "\n");
    for (Match match : matches()) {
      stringBuilder.append(match).append("\n");
    }
    return stringBuilder.toString();
  }
}
