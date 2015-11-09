package com.felipecsl.elifut.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class ClubStats implements Parcelable {
  public abstract int points();
  public abstract int wins();
  public abstract int draws();
  public abstract int losses();
  public abstract int goals();

  public static ClubStats create() {
    return new AutoValue_ClubStats(0, 0, 0, 0, 0);
  }

  public static Builder builder() {
    return new AutoValue_ClubStats.Builder()
        .points(0)
        .wins(0)
        .draws(0)
        .losses(0)
        .goals(0);
  }

  public ClubStats newWithWin() {
    return toBuilder()
        .points(points() + 3)
        .wins(wins() + 1)
        .build();
  }

  public ClubStats newWithDraw() {
    return toBuilder()
        .points(points() + 1)
        .draws(draws() + 1)
        .build();
  }

  public ClubStats newWithLoss() {
    return toBuilder()
        .losses(losses() + 1)
        .build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder points(int i);
    public abstract Builder wins(int i);
    public abstract Builder draws(int i);
    public abstract Builder losses(int i);
    public abstract Builder goals(int i);
    public abstract ClubStats build();
  }

  public Builder toBuilder() {
    // https://github.com/google/auto/issues/281
    return new AutoValue_ClubStats.Builder(this);
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_ClubStats.typeAdapterFactory();
  }

  @Override public int describeContents() {
    return 0;
  }
}
