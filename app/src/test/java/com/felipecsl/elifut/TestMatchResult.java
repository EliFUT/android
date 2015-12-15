package com.felipecsl.elifut;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.MatchResult;

import java.util.List;

class TestMatchResult extends MatchResult {
  @Nullable @Override public Club winner() {
    return null;
  }

  @Nullable @Override public Club loser() {
    return null;
  }

  @Override public boolean isDraw() {
    return false;
  }

  @Override public boolean isHomeWin() {
    return false;
  }

  @Override public boolean isAwayWin() {
    return false;
  }

  @Override public List<Goal> homeGoals() {
    return null;
  }

  @Override public List<Goal> awayGoals() {
    return null;
  }

  @Override public String finalScore() {
    return null;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }

  public static final Parcelable.Creator<TestMatchResult> CREATOR =
      new Parcelable.Creator<TestMatchResult>() {
        @java.lang.Override
        public TestMatchResult createFromParcel(Parcel in) {
          return new TestMatchResult();
        }

        @java.lang.Override
        public TestMatchResult[] newArray(int size) {
          return new TestMatchResult[size];
        }
      };
}
