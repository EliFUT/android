package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class Club extends Model {
  public abstract String name();
  @Nullable public abstract String abbrev_name();
  public abstract String small_image();
  public abstract String large_image();
  public abstract int league_id();
  public abstract ClubStats stats();

  public static Builder builder() {
    return new AutoValue_Club.Builder()
        .stats(ClubStats.builder().build())
        .small_image("")
        .large_image("")
        .base_id(0)
        .league_id(0);
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
    return toBuilder().stats(stats().newWithWin()).build();
  }

  public Club newWithDraw() {
    return toBuilder().stats(stats().newWithDraw()).build();
  }

  public Club newWithLoss() {
    return toBuilder().stats(stats().newWithLoss()).build();
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
