package com.felipecsl.elifut.models;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

import java.util.Arrays;
import java.util.List;

@AutoValue
public abstract class Player extends Model implements Persistable {
  public static final List<String> DEFENDER_POSITIONS =
      Arrays.asList("RB", "RWB", "CB", "LB", "LWB");
  public static final List<String> MIDFIELDER_POSITIONS =
      Arrays.asList("RM", "CDM", "CM", "CAM", "LM");
  public static final List<String> ATTACKER_POSITIONS =
      Arrays.asList("RW", "CF", "ST", "LW");
  // Not sent directly from the API, but manually filled after retrieval
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

  public static Builder builder() {
    return new AutoValue_Player.Builder();
  }

  public Builder toBuilder() {
    // https://github.com/google/auto/issues/281
    return new AutoValue_Player.Builder(this);
  }

  public static Player create(Cursor cursor) {
    return AutoValue_Player.createFromCursor(cursor);
  }

  enum Foot {
    Left, Right
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Player.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
