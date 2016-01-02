package com.felipecsl.elifut.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class LeagueRound implements Parcelable, Persistable {
  // id becomes non-null after it has been persisted to the DB and auto-assigned an id
  @Nullable public abstract Integer id();
  public abstract int roundNumber();
  public abstract List<Match> matches();

  public static LeagueRound create(Integer id, int roundNumber, List<Match> matches) {
    return new AutoValue_LeagueRound(id, roundNumber, matches);
  }

  public static LeagueRound create(int roundNumber, List<Match> matches) {
    return create(null, roundNumber, matches);
  }

  public static LeagueRound create(int roundNumber) {
    return create(roundNumber, new ArrayList<>());
  }

  @Override public int describeContents() {
    return 0;
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

  // Roll our own equal() and hashCode() since we don't want to include the ID in it
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LeagueRound that = (LeagueRound) o;

    //noinspection SimplifiableIfStatement
    if (roundNumber() != that.roundNumber()) return false;
    return matches().equals(that.matches());
  }

  @Override public int hashCode() {
    int result = roundNumber();
    result = 31 * result + matches().hashCode();
    return result;
  }
}
