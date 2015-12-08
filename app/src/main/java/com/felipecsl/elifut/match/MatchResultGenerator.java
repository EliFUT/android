package com.felipecsl.elifut.match;

import android.util.Log;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.Goals;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import java.util.Collections;
import java.util.List;

public class MatchResultGenerator {
  private static final String TAG = MatchResultGenerator.class.getSimpleName();
  private final RandomGenerator random;
  private final RealDistribution goalsDistribution;

  public MatchResultGenerator() {
    this(new Well19937c(), MatchResult.GOALS_DISTRIBUTION);
  }

  public MatchResultGenerator(RandomGenerator random, RealDistribution goalsDistribution) {
    this.random = random;
    this.goalsDistribution = goalsDistribution;
  }

  public MatchResult generate(Match match) {
    float result = random.nextFloat();
    MatchResult.Builder matchResult = MatchResult.builder().match(match);
    Club winner;
    List<Goal> winnerGoals;
    List<Goal> loserGoals;
    Log.d(TAG, "winner result was " + result);

    if (result <= MatchResult.HOME_WIN_PROBABILITY) {
      winner = match.home();
    } else if (result <= MatchResult.DRAW_PROBABILITY) {
      winner = null;
    } else {
      winner = match.away();
    }
    boolean isHomeWin = match.home().equals(winner);
    Club loser = isHomeWin ? match.away() : match.home();
    int totalGoals = Math.max((int) Math.round(goalsDistribution.sample()), 0);

    if (winner != null) {
      if (totalGoals <= 2) {
        winnerGoals = Goals.create(random, Math.max(totalGoals, 1), winner);
        loserGoals = Collections.emptyList();
      } else {
        // 3+ goals
        loserGoals =
            Goals.create(random, random.nextInt(Math.max(1, (totalGoals / 2) - 1)), loser);
        winnerGoals = Goals.create(random, totalGoals - loserGoals.size(), winner);
      }
    } else {
      int evenGoals = (totalGoals % 2 == 0) ? totalGoals : totalGoals + 1;
      winnerGoals = Goals.create(random, evenGoals / 2, match.home());
      loserGoals = Goals.create(random, evenGoals / 2, match.away());
    }

    List<Goal> homeGoals = isHomeWin ? winnerGoals : loserGoals;
    List<Goal> awayGoals = isHomeWin ? loserGoals : winnerGoals;

    return matchResult
        .match(match)
        .winner(winner)
        .loser(winner != null ? loser : null)
        .homeGoals(homeGoals)
        .awayGoals(awayGoals)
        .isDraw(winner == null)
        .isHomeWin(isHomeWin)
        .isAwayWin(!isHomeWin)
        .finalScore(homeGoals.size() + "x" + awayGoals.size())
        .build();
  }
}
