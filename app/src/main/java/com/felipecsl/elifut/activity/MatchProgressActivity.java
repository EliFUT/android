package com.felipecsl.elifut.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.ResponseObserver;
import com.felipecsl.elifut.match.MatchResultController;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.LeagueRound;
import com.felipecsl.elifut.models.Match;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.preferences.LeagueDetails;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.felipecsl.elifut.widget.FractionView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MatchProgressActivity extends ElifutActivity {
  private static final String TAG = MatchProgressActivity.class.getSimpleName();
  private static final String EXTRA_ROUND = "EXTRA_ROUND";

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.img_team_home) ImageView imgTeamHome;
  @BindView(R.id.img_team_away) ImageView imgTeamAway;
  @BindView(R.id.txt_team_home) TextView txtTeamHome;
  @BindView(R.id.txt_team_away) TextView txtTeamAway;
  @BindView(R.id.txt_team_home_goals) TextView txtTeamHomeGoals;
  @BindView(R.id.events_layout) LinearLayout eventsLayout;
  @BindView(R.id.events_timeline) View eventsTimeline;
  @BindView(R.id.events_scrollview) ScrollView eventsScrollView;
  @BindView(R.id.txt_team_away_goals) TextView txtTeamAwayGoals;
  @BindView(R.id.fractionView) FractionView fractionView;
  @BindView(R.id.fab_play_pause) FloatingActionButton playPauseButton;
  @BindView(R.id.fab_done) FloatingActionButton doneButton;
  @BindString(R.string.end_first_half) String strEndOfFirstHalf;
  @BindString(R.string.end_match) String strEndOfMatch;
  @BindDimen(R.dimen.match_event_icon_size) int iconSize;
  @BindDimen(R.dimen.match_event_icon_padding) int iconPadding;

  @Inject UserPreferences userPreferences;
  @Inject LeagueDetails leagueDetails;

  @State LeagueRound round;
  @State boolean isRunning;
  @State int elapsedMinutes;

  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private String finalScoreMessage;
  private int finalScoreIcon;
  private Match match;
  private Club userClub;
  private MatchResult matchResult;
  private final Observer<Club> clubObserver =
      new ResponseObserver<Club>(this, TAG, "Failed to load club") {
        @Override public void onNext(Club response) {
          ImageView imgView;
          TextView txtView;
          if (response.nameEquals(match.home())) {
            imgView = imgTeamHome;
            txtView = txtTeamHome;
          } else {
            imgView = imgTeamAway;
            txtView = txtTeamAway;
          }
          Picasso.with(MatchProgressActivity.this)
              .load(response.large_image())
              .into(imgView);

          txtView.setText(response.shortName());
        }

        @Override public void onCompleted() {
          if (!matchResult.isDraw()) {
            Club winner = matchResult.winner();
            //noinspection ConstantConditions
            boolean isWinner = winner.nameEquals(userClub);
            finalScoreIcon = isWinner ? R.drawable.ic_mood_black_48px
                : R.drawable.ic_sentiment_very_dissatisfied_black_48px;
            if (isWinner) {
              finalScoreMessage = getString(R.string.winner);
            } else {
              finalScoreMessage = getString(R.string.defeated);
            }
          } else {
            finalScoreIcon = R.drawable.ic_sentiment_neutral_black_48px;
            finalScoreMessage = getString(R.string.draw);
          }
          if (BuildConfig.DEBUG) {
            Log.d(TAG, finalScoreMessage);
          }

          startTimer();
        }
      };

  public static Intent newIntent(Context context, LeagueRound round) {
    return new Intent(context, MatchProgressActivity.class)
        .putExtra(EXTRA_ROUND, round);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    setContentView(R.layout.activity_match_progress);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      round = intent.getParcelableExtra(EXTRA_ROUND);
    }
    userClub = userPreferences.club();
    match = round.findMatchByClub(userClub);
    matchResult = match.result();
    loadClubs(match.home().id(), match.away().id());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    stopTimer();
    MatchResultController controller = new MatchResultController(userPreferences);
    controller.updateWithResult(matchResult);
  }

  private void loadClubs(int homeId, int awayId) {
    Subscription subscription = clubObservable(homeId)
        .mergeWith(clubObservable(awayId))
        .subscribe(clubObserver);

    subscriptions.add(subscription);
  }

  private Observable<Club> clubObservable(int id) {
    return service.club(id).compose(this.<Club>applyTransformations());
  }

  private void stopTimer() {
    subscriptions.clear();
    isRunning = false;
  }

  private void startTimer() {
    subscriptions.add(matchResult.eventsObservable(elapsedMinutes)
        .map(matchEvent -> (Goal) matchEvent)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(goal -> {
          boolean isHomeGoal = goal.club().nameEquals(match.home());
          TextView txtScore = isHomeGoal ? txtTeamHomeGoals : txtTeamAwayGoals;
          int currGoals = Integer.parseInt(txtScore.getText().toString());
          txtScore.setText(String.valueOf(++currGoals));
          int gravity = isHomeGoal ? GravityCompat.START : GravityCompat.END;
          appendEvent(R.drawable.ball, goal.time() + "' " + goal.player().name(), gravity);
        }));

    subscriptions.add(timerObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(l -> {
          elapsedMinutes++;
          fractionView.setFraction(elapsedMinutes % 45, 60);
          if (elapsedMinutes == 45) {
            appendEvent(R.drawable.ic_schedule_black_48px, strEndOfFirstHalf, Gravity.CENTER);
          } else if (elapsedMinutes == 90) {
            stopTimer();
            appendEvent(R.drawable.ic_schedule_black_48px, strEndOfMatch, Gravity.CENTER);
            appendEvent(finalScoreIcon, finalScoreMessage, Gravity.CENTER);
            Club winner = matchResult.winner();
            boolean isDraw = matchResult.isDraw();
            boolean isWinner = !isDraw && userClub.nameEquals(winner);
            if (isDraw || isWinner) {
              appendEvent(R.drawable.ic_attach_money_black_24dp, "+" +
                      (isWinner ? UserPreferences.COINS_PRIZE_WIN : UserPreferences.COINS_PRIZE_DRAW),
                  Gravity.CENTER_HORIZONTAL);
            }
            playPauseButton.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
            fractionView.setFraction(45, 60);
          }
        }));
    isRunning = true;
  }

  @NonNull private Observable<Long> timerObservable() {
    return BuildConfig.DEBUG ? Observable.interval(0, 100, TimeUnit.MILLISECONDS)
        : Observable.interval(0, 1, TimeUnit.SECONDS);
  }

  private void appendEvent(@DrawableRes int icon, String text, int gravity) {
    FrameLayout view = (FrameLayout) LayoutInflater.from(this)
        .inflate(R.layout.match_event, eventsLayout, false);
    TextView textView = (TextView) view.findViewById(R.id.txt_match_events);
    textView.setText(text);
    Drawable drawable = ContextCompat.getDrawable(this, icon);
    drawable.setBounds(0, 0, iconSize, iconSize);
    textView.setCompoundDrawablePadding(iconPadding);
    if (gravity == GravityCompat.START) {
      textView.setCompoundDrawables(null, null, drawable, null);
    } else {
      textView.setCompoundDrawables(drawable, null, null, null);
    }
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
    layoutParams.gravity = gravity;
    view.setLayoutParams(layoutParams);
    textView.setGravity(gravity);
    eventsLayout.addView(view, 0);
    eventsTimeline.setVisibility(View.VISIBLE);
    eventsScrollView.smoothScrollTo(0, 0);
  }

  @OnClick(R.id.fab_play_pause) void onClickPause() {
    if (isRunning) {
      stopTimer();
      Snackbar.make(playPauseButton, R.string.match_paused, Snackbar.LENGTH_SHORT).show();
      playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_48dp);
    } else {
      startTimer();
      Snackbar.make(playPauseButton, R.string.match_resumed, Snackbar.LENGTH_SHORT).show();
      playPauseButton.setImageResource(R.drawable.ic_pause_white_48dp);
    }
  }

  @OnClick(R.id.fab_done) void onClickDone() {
    finish();
    startActivity(LeagueRoundResultsActivity.newIntent(this, round));
  }
}