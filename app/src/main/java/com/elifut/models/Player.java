package com.elifut.models;

import com.google.auto.value.AutoValue;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@AutoValue
public abstract class Player extends Model implements Persistable {
  static final Comparator<Player> PLAYER_COMPARATOR = (c1, c2) -> c2.rating() - c1.rating();
  static final List<String> DEFENDER_POSITIONS =
      Arrays.asList("RB", "RWB", "CB", "LB", "LWB");
  static final List<String> MIDFIELDER_POSITIONS =
      Arrays.asList("RM", "CDM", "CM", "CAM", "LM");
  static final List<String> ATTACKER_POSITIONS =
      Arrays.asList("RW", "CF", "ST", "LW");
  private static final List<String> VALID_COLORS =
      Arrays.asList("bronze", "silver", "gold", "rare_bronze", "rare_silver", "rare_gold");
  static final List<String> GOALKEEPER_POSITIONS =
      Collections.singletonList("GK");

  // Not sent directly from the API, but manually filled after retrieval
  // @formatter:off
  @ColumnName("club_id") @Nullable public abstract Integer clubId();
  public abstract String first_name();
  public abstract String last_name();
  public abstract String name();
  @Nullable public abstract String common_name();
  public abstract String position();
  public abstract String image();
  public abstract String nation_image();
  public abstract int rating();
  public abstract String player_type();
  public abstract int attribute_1();
  public abstract int attribute_2();
  public abstract int attribute_3();
  public abstract int attribute_4();
  public abstract int attribute_5();
  public abstract int attribute_6();
  public abstract String quality();
  public abstract String color();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder id(int i);
    public abstract Builder base_id(int i);
    public abstract Builder clubId(Integer i);
    public abstract Builder first_name(String x);
    public abstract Builder last_name(String x);
    public abstract Builder name(String x);
    public abstract Builder common_name(String x);
    public abstract Builder position(String x);
    public abstract Builder image(String x);
    public abstract Builder nation_image(String x);
    public abstract Builder rating(int x);
    public abstract Builder player_type(String x);
    public abstract Builder attribute_1(int x);
    public abstract Builder attribute_2(int x);
    public abstract Builder attribute_3(int x);
    public abstract Builder attribute_4(int x);
    public abstract Builder attribute_5(int x);
    public abstract Builder attribute_6(int x);
    public abstract Builder quality(String x);
    public abstract Builder color(String x);
    public abstract Player build();
  }
  // @formatter:on

  public static Builder builder() {
    return new AutoValue_Player.Builder();
  }

  public abstract Builder toBuilder();

  public static Player create(Cursor cursor) {
    return AutoValue_Player.createFromCursor(cursor);
  }

  public abstract ContentValues toContentValues();

  public boolean isValidColor() {
    return VALID_COLORS.indexOf(color()) != -1;
  }

  public static JsonAdapter<Player> jsonAdapter(Moshi moshi) {
    return new AutoValue_Player.MoshiJsonAdapter(moshi);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public String toString() {
    return rating() + " " + position() + " " + name();
  }
}
