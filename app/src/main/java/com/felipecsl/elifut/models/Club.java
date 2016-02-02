package com.felipecsl.elifut.models;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.gabrielittner.auto.value.cursor.CursorAdapter;
import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import com.squareup.moshi.JsonAdapter;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Club extends Model implements Persistable {
  public abstract String name();
  public abstract String small_image();
  public abstract String large_image();
  public abstract int league_id();
  @Nullable public abstract String abbrev_name();
  @CursorAdapter(ClubStats.class) @Nullable public abstract ClubStats stats();

  public static Club create(Cursor cursor) {
    return AutoValue_Club.createFromCursor(cursor);
  }

  /**
   * Returns a list of all the players on this team who are substitutes, that is, who are not
   * on the main squad of 11 players.
   */
  public List<? extends Player> substitutes(ElifutDataStore dataStore) {
    String clubId = String.valueOf(id());
    List<? extends Player> players = dataStore.query(
        AutoValueClasses.PLAYER, "club_id = ?", clubId);
    ClubSquad clubSquad = Preconditions.checkNotNull(dataStore.queryOne(
        AutoValueClasses.CLUB_SQUAD, "club_id = ?", clubId));
    // Exclude the players who are already on the team squad from the list of all players, so we
    // get a list with only the subs.
    return FluentIterable.from(players)
        .filter(Predicates.not(Predicates.in(clubSquad.squad())))
        .toList();
  }

  /** Replaces an existing player on this Club's squad with a new one and saves the changes. */
  public void replacePlayer(Player oldPlayer, Player newPlayer, ElifutDataStore dataStore) {
    List<? extends ClubSquad> clubSquads = dataStore.query(
        AutoValueClasses.CLUB_SQUAD, "club_id = ?", String.valueOf(id()));
    ClubSquad clubSquad = clubSquads.get(
        Preconditions.checkElementIndex(0, clubSquads.size(), "Club squad not found"));
    List<Player> squad = new ArrayList<>(clubSquad.squad());
    squad.remove(oldPlayer);
    squad.add(newPlayer);
    dataStore.update(clubSquad.toBuilder().squad(squad).build(), clubSquad.id());
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

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Club.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
