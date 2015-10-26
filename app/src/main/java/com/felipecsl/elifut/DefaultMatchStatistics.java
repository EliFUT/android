package com.felipecsl.elifut;

import android.support.annotation.Nullable;

import com.felipecsl.elifut.models.Club;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class DefaultMatchStatistics implements MatchStatistics {
  private final Club home;
  private final Club away;

  @Nullable private Club winner;
  private int winnerGoals;
  private int loserGoals;

  public DefaultMatchStatistics(Club home, Club away) {
    this(home, away, new Well19937c(), MatchStatistics.GOALS_DISTRIBUTION);
  }

  public DefaultMatchStatistics(Club home, Club away, RandomGenerator winnerGenerator,
      RealDistribution goalsDistribution) {
    this.home = home;
    this.away = away;

    float result = winnerGenerator.nextFloat();
    if (result <= MatchStatistics.HOME_WIN_PROBABILITY) {
      winner = home;
    } else if (result <= MatchStatistics.DRAW_PROBABILITY) {
      winner = null;
    } else {
      winner = away;
    }
    int totalGoals = (int) Math.round(goalsDistribution.sample());
    if (!isDraw()) {
      if (totalGoals <= 2) {
        winnerGoals = totalGoals;
        loserGoals = 0;
      } else {
        // 3+ goals
        loserGoals = winnerGenerator.nextInt(totalGoals);
        winnerGoals = totalGoals - loserGoals;
      }
    } else {
      int evenGoals = (totalGoals % 2 == 0) ? totalGoals : totalGoals + 1;
      winnerGoals = evenGoals / 2;
      loserGoals = evenGoals / 2;
    }
  }

  @Override public Club home() {
    return home;
  }

  @Override public Club away() {
    return away;
  }

  @Override public Club winner() {
    return winner;
  }

  @Override @Nullable public Club loser() {
    if (isDraw()) {
      return null;
    }
    return isHomeWin() ? away : home;
  }

  @Override public boolean isHomeWin() {
    if (isDraw()) {
      return false;
    }

    return winner.equals(home);
  }

  @Override public boolean isAwayWin() {
    if (isDraw()) {
      return false;
    }

    return winner.equals(away);
  }

  @Override public boolean isDraw() {
    return winner == null;
  }

  @Override public String finalScore() {
    return homeGoals() + "x" + awayGoals();
  }

  @Override public int homeGoals() {
    return isHomeWin() ? winnerGoals : loserGoals;
  }

  @Override public int awayGoals() {
    return isAwayWin() ? winnerGoals : loserGoals;
  }

  @Override public String refereeName() {
    return "John Doe";
  }

  @Override public ClubStatistics homeStatistics() {
    return ClubStatistics.create(0, 0, 0, 0, 0, 0, 0, 0);
  }

  @Override public ClubStatistics awayStatistics() {
    return ClubStatistics.create(0, 0, 0, 0, 0, 0, 0, 0);
  }
}
