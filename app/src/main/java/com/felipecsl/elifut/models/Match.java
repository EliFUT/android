package com.felipecsl.elifut.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Match implements Parcelable, Persistable {
  // id becomes non-null after it has been persisted to the DB and auto-assigned an id
  @Nullable public abstract Integer id();
  public abstract Club home();
  public abstract Club away();
  public abstract MatchResult result();

  public static Match create(Club home, Club away, MatchResult result) {
    return create(null, home, away, result);
  }

  public static Match create(Integer id, Club home, Club away, MatchResult result) {
    return new AutoValue_Match(id, home, away, result);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public String toString() {
    return home().name()
        + " "
        + result().homeGoals().size()
        + " X "
        + result().awayGoals().size()
        + " "
        + away().name();
  }

  public boolean hasClub(Club club) {
    return home().nameEquals(club) || away().nameEquals(club);
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Match.typeAdapterFactory();
  }

  // Roll our own equal() and hashCode() since we don't want to include the ID in it
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Match match = (Match) o;

    if (!home().equals(match.home())) return false;
    //noinspection SimplifiableIfStatement
    if (!away().equals(match.away())) return false;
    return result().equals(match.result());
  }

  @Override public int hashCode() {
    int result1 = home().hashCode();
    result1 = 31 * result1 + away().hashCode();
    result1 = 31 * result1 + result().hashCode();
    return result1;
  }
}
