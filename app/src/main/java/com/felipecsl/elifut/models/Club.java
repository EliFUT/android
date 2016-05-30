package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.FluentIterable;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.services.ClubDataStore;
import com.gabrielittner.auto.value.cursor.CursorAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

@AutoValue
public abstract class Club extends Model implements Persistable {
  // @formatter:off
  public abstract String name();
  public abstract String small_image();
  public abstract String large_image();
  public abstract int league_id();
  @Nullable public abstract String abbrev_name();
  @CursorAdapter(ClubStats.class) @Nullable public abstract ClubStats stats();
  // @formatter:on

  public static Club create(Cursor cursor) {
    return AutoValue_Club.createFromCursor(cursor);
  }

  @Override public String toString() {
    return shortName();
  }

  /**
   * Returns a list of all the players on this team who are substitutes, that is, who are not on the
   * main squad of 11 players.
   */
  public List<? extends Player> substitutes(ClubDataStore dataStore) {
    List<? extends Player> players = dataStore.allPlayers(this);
    ClubSquad clubSquad = dataStore.squad(this);
    // Exclude the players who are already on the team players from the list of all players, so we
    // get a list with only the subs.
    return FluentIterable.from(players)
        .filter(not(in(clubSquad.players())))
        .toList();
  }

  /** Replaces an existing player on this Club's players with a new one and saves the changes. */
  public void replacePlayer(Player oldPlayer, Player newPlayer, ClubDataStore dataStore) {
    ClubSquad clubSquad = dataStore.squad(this);
    List<Player> squad = new ArrayList<>(clubSquad.players());
    squad.remove(oldPlayer);
    squad.add(newPlayer);
    dataStore.updateSquad(clubSquad, clubSquad.toBuilder().players(squad).build());
  }

  public static Builder builder() {
    return new AutoValue_Club.Builder()
        .stats(ClubStats.builder().build())
        .small_image("")
        .large_image("")
        .base_id(0)
        .league_id(0);
  }

  public static Club create(int id, String name) {
    return Club.builder().id(id).name(name).build();
  }

  public String tinyName() {
    //noinspection ConstantConditions
    return abbrev_name() != null ? abbrev_name().substring(0, 3) : name().substring(0, 3);
  }

  // @formatter:off
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder id(int i);
    public abstract Builder base_id(int i);
    public abstract Builder name(String s);
    public abstract Builder abbrev_name(String s);
    public abstract Builder small_image(String s);
    public abstract Builder large_image(String s);
    public abstract Builder league_id(int i);
    public abstract Builder stats(ClubStats s);
    public abstract Club build();
  }
  // @formatter:on

  public Builder toBuilder() {
    // https://github.com/google/auto/issues/281
    return new AutoValue_Club.Builder(this);
  }

  public Club newWithWin() {
    return toBuilder()
        .stats(nonNullStats().newWithWin())
        .build();
  }

  public boolean nameEquals(Club otherTeam) {
    return name().equals(otherTeam.name());
  }

  public Club newWithDraw() {
    return toBuilder().stats(nonNullStats().newWithDraw()).build();
  }

  public Club newWithLoss() {
    return toBuilder().stats(nonNullStats().newWithLoss()).build();
  }

  @NonNull public ClubStats nonNullStats() {
    //noinspection ConstantConditions
    return stats() == null ? ClubStats.create() : stats();
  }

  public String shortName() {
    return abbrev_name() != null ? abbrev_name() : name();
  }

  public static JsonAdapter<Club> jsonAdapter(Moshi moshi) {
    return new AutoValue_Club.MoshiJsonAdapter(moshi);
  }

  @Override public int describeContents() {
    return 0;
  }
}
