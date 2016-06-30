package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.gabrielittner.auto.value.cursor.ColumnTypeAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ClubStats implements Parcelable {
  public abstract int points();
  public abstract int wins();
  public abstract int draws();
  public abstract int losses();
  public abstract int goals();

  public static ClubStats create(Cursor cursor) {
    return AutoValue_ClubStats.createFromCursor(cursor);
  }

  public abstract ContentValues toContentValues();

  public int games() {
    return wins() + draws() + losses();
  }

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

  public ClubStats newWithWin(int goalsDifferential) {
    return toBuilder()
        .points(points() + 3)
        .wins(wins() + 1)
        .goals(goals() + goalsDifferential)
        .build();
  }

  public ClubStats newWithDraw() {
    return toBuilder()
        .points(points() + 1)
        .draws(draws() + 1)
        .build();
  }

  public ClubStats newWithLoss(int goalsDifferential) {
    return toBuilder()
        .losses(losses() + 1)
        .goals(goals() + goalsDifferential)
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

  public static JsonAdapter<ClubStats> jsonAdapter(Moshi moshi) {
    return new AutoValue_ClubStats.MoshiJsonAdapter(moshi);
  }

  @Override public int describeContents() {
    return 0;
  }

  static class Adapter implements ColumnTypeAdapter<ClubStats> {
    @Override public ClubStats fromCursor(Cursor cursor, String columnName) {
      return ClubStats.create(cursor);
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, ClubStats value) {
      if (value != null) {
        values.putAll(value.toContentValues());
      }
    }
  }
}
