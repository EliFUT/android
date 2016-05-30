package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Match implements Parcelable, Persistable {
  // id becomes non-null after it has been persisted to the DB and auto-assigned an id
  @Nullable public abstract Integer id();
  public abstract Club home();
  public abstract Club away();
  // Result is initially null and is only computed when the match is about to start
  @Nullable public abstract MatchResult result();

  public static Match create(Club home, Club away) {
    return new AutoValue_Match(null, home, away, null);
  }

  public static Match create(Integer id, Club home, Club away, MatchResult matchResult) {
    return new AutoValue_Match(id, home, away, matchResult);
  }

  public static Match create(Club home, Club away, MatchResult matchResult) {
    return new AutoValue_Match(null, home, away, matchResult);
  }

  public static Match create(Integer id, Club home, Club away) {
    return new AutoValue_Match(id, home, away, null);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public String toString() {
    if (result() != null) {
      return home().name()
          + " "
          + result().homeGoals().size()
          + " X "
          + result().awayGoals().size()
          + " "
          + away().name();
    }
    return home().name() + " X " + away().name();
  }

  public boolean hasClub(Club club) {
    return home().nameEquals(club) || away().nameEquals(club);
  }

  public static JsonAdapter<Match> jsonAdapter(Moshi moshi) {
    return new AutoValue_Match.MoshiJsonAdapter(moshi);
  }

  // Roll our own equal() and hashCode() since we don't want to include the ID in it
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Match matchTwo = (Match) o;

    if (!home().equals(matchTwo.home())) return false;
    //noinspection SimplifiableIfStatement
    if (!away().equals(matchTwo.away())) return false;
    //noinspection ConstantConditions
    return result() != null ? result().equals(matchTwo.result()) : matchTwo.result() == null;

  }

  @Override public int hashCode() {
    int result1 = home().hashCode();
    result1 = 31 * result1 + away().hashCode();
    //noinspection ConstantConditions
    result1 = 31 * result1 + (result() != null ? result().hashCode() : 0);
    return result1;
  }
}
