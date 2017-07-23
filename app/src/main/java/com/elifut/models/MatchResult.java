package com.elifut.models;

import com.google.auto.value.AutoValue;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.elifut.BuildConfig;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

@AutoValue
public abstract class MatchResult implements Parcelable, Persistable {
  public static final float HOME_WIN_PROBABILITY = .465f;
  public static final float DRAW_PROBABILITY = .174f;
  public static final NormalDistribution GOALS_DISTRIBUTION = new NormalDistribution(2.6, 1.7);

  @Nullable public abstract Club winner();
  @Nullable public abstract Club loser();
  public abstract boolean isDraw();
  public abstract boolean isHomeWin();
  public abstract boolean isAwayWin();
  public abstract List<Goal> homeGoals();
  public abstract List<Goal> awayGoals();
  public abstract String finalScore();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder homeGoals(List<Goal> x);
    public abstract Builder awayGoals(List<Goal> x);
    abstract MatchResult autoBuild();
    abstract Builder winner(Club x);
    abstract Builder loser(Club x);
    abstract Builder isDraw(boolean x);
    abstract Builder isHomeWin(boolean x);
    abstract Builder isAwayWin(boolean x);
    abstract Builder finalScore(String x);
    abstract List<Goal> homeGoals();
    abstract List<Goal> awayGoals();

    public MatchResult build(Club home, Club away) {
      Club winner = null;
      Club loser = null;
      boolean isDraw = false;
      boolean isHomeWin = false;
      boolean isAwayWin = false;
      int awayGoals = awayGoals().size();
      int homeGoals = homeGoals().size();
      if (homeGoals != awayGoals) {
        winner = homeGoals > awayGoals ? home: away;
        loser = homeGoals > awayGoals ? away : home;
        isHomeWin = homeGoals > awayGoals;
        isAwayWin = !isHomeWin;
      } else {
        isDraw = true;
      }
      return winner(winner)
          .loser(loser)
          .isDraw(isDraw)
          .isHomeWin(isHomeWin)
          .isAwayWin(isAwayWin)
          .finalScore(homeGoals + "x" + awayGoals)
          .autoBuild();
    }
  }

  public static Builder builder() {
    return new AutoValue_MatchResult.Builder()
        .homeGoals(Collections.emptyList())
        .awayGoals(Collections.emptyList())
        .isHomeWin(false)
        .isAwayWin(false)
        .isDraw(false);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static JsonAdapter<MatchResult> jsonAdapter(Moshi moshi) {
    return new AutoValue_MatchResult.MoshiJsonAdapter(moshi);
  }

  /**
   * Returns an {@link Observable} that emits the all the {@link MatchEvent} of this result in this
   * match at the correct time that they happened.
   */
  public Observable<MatchEvent> eventsObservable(final int elapsedTime) {
    Observable<Goal> homeGoalsObservable = Observable.from(homeGoals());
    Observable<Goal> awayGoalsObservable = Observable.from(awayGoals());

    return Observable.merge(homeGoalsObservable, awayGoalsObservable)
        .observeOn(Schedulers.io())
        .flatMap((goal) -> {
          int delay = goal.time() - elapsedTime;
          if (BuildConfig.DEBUG) {
            delay /= 10;
          }
          return Observable.<MatchEvent>just(goal).delay(delay, TimeUnit.SECONDS);
        }).skip(elapsedTime, TimeUnit.SECONDS);
  }
}
