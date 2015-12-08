package com.felipecsl.elifut.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.BuildConfig;
import com.google.auto.value.AutoValue;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

@AutoValue
public abstract class MatchResult implements Parcelable {
  public static final float HOME_WIN_PROBABILITY = .465f;
  public static final float DRAW_PROBABILITY = HOME_WIN_PROBABILITY + .174f;
  public static final NormalDistribution GOALS_DISTRIBUTION = new NormalDistribution(2.6, 1.7);

  public abstract Match match();
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
    public abstract Builder match(Match x);
    public abstract Builder winner(Club x);
    public abstract Builder loser(Club x);
    public abstract Builder isDraw(boolean x);
    public abstract Builder isHomeWin(boolean x);
    public abstract Builder isAwayWin(boolean x);
    public abstract Builder homeGoals(List<Goal> x);
    public abstract Builder awayGoals(List<Goal> x);
    public abstract Builder finalScore(String x);
    public abstract MatchResult build();
  }

  public static Builder builder() {
    return new AutoValue_MatchResult.Builder();
  }

  @Override public int describeContents() {
    return 0;
  }

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
