package com.felipecsl.elifut;

import android.support.annotation.Nullable;

import com.felipecsl.elifut.models.Club;

import org.apache.commons.math3.distribution.NormalDistribution;

public interface MatchStatistics {
  public static final float HOME_WIN_PROBABILITY = .465f;
  public static final float DRAW_PROBABILITY = HOME_WIN_PROBABILITY + .174f;
  public static final NormalDistribution GOALS_DISTRIBUTION = new NormalDistribution(2.6, 1.7);

  Club home();
  Club away();
  @Nullable Club winner();
  @Nullable Club loser();
  boolean isDraw();
  boolean isHomeWin();
  boolean isAwayWin();
  int homeGoals();
  int awayGoals();
  String finalScore();
  String refereeName();
  ClubStatistics homeStatistics();
  ClubStatistics awayStatistics();
}
