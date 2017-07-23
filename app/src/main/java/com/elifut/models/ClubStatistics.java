package com.elifut.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ClubStatistics {
  public abstract int goals();
  public abstract int shots();
  public abstract int shotsOnTarget();
  public abstract int corners();
  public abstract int offsides();
  public abstract int fouls();
  public abstract int yellowCards();
  public abstract int redCards();

  public static ClubStatistics create(int goals, int shots, int shotsOnTarget, int corners,
      int offsides, int fouls, int yellowCards, int redCards) {
    return new AutoValue_ClubStatistics(goals, shots, shotsOnTarget, corners, offsides, fouls,
        yellowCards, redCards);
  }
}
