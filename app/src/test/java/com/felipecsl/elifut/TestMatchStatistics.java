package com.felipecsl.elifut;

import android.support.annotation.Nullable;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;

import java.util.List;

class TestMatchStatistics implements MatchStatistics {
  @Override public Club home() {
    return null;
  }

  @Override public Club away() {
    return null;
  }

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

  @Override public String refereeName() {
    return null;
  }

  @Override public ClubStatistics homeStatistics() {
    return null;
  }

  @Override public ClubStatistics awayStatistics() {
    return null;
  }
}
