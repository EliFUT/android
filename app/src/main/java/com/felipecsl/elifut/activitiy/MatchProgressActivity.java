package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.elifut.DefaultMatchStatistics;
import com.felipecsl.elifut.ElifutPreferences;
import com.felipecsl.elifut.MatchStatistics;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.League;
import com.felipecsl.elifut.models.MatchEvent;
import com.felipecsl.elifut.widget.FractionView;
import com.squareup.picasso.Picasso;

import org.apache.commons.math3.random.Well19937c;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class MatchProgressActivity extends ElifutActivity {
  private static final String EXTRA_CLUB_HOME = "EXTRA_CLUB_HOME";
  private static final String EXTRA_CLUB_AWAY = "EXTRA_CLUB_AWAY";
  private static final String TAG = MatchProgressActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.img_team_home) ImageView imgTeamHome;
  @Bind(R.id.img_team_away) ImageView imgTeamAway;
  @Bind(R.id.txt_team_home) TextView txtTeamHome;
  @Bind(R.id.txt_team_away) TextView txtTeamAway;
  @Bind(R.id.txt_match_events) TextView txtMatchEvents;
  @Bind(R.id.txt_team_home_goals) TextView txtTeamHomeGoals;
  @Bind(R.id.txt_team_away_goals) TextView txtTeamAwayGoals;
  @Bind(R.id.fractionView) FractionView fractionView;
  @Bind(R.id.fab_play_pause) FloatingActionButton playPauseButton;
  @Bind(R.id.fab_done) FloatingActionButton doneButton;
  @BindString(R.string.end_first_half) String strEndOfFirstHalf;
  @BindString(R.string.end_match) String strEndOfMatch;

  @Inject ElifutPreferences preferences;

  @State Club home;
  @State Club away;
  @State boolean isRunning;
  @State int elapsedMinutes;
  @State DefaultMatchStatistics statistics;

  private CompositeSubscription subscriptions = new CompositeSubscription();
  private String finalScoreMessage;

  public static Intent newIntent(Context context, Club home, Club away) {
    return new Intent(context, MatchProgressActivity.class)
        .putExtra(EXTRA_CLUB_HOME, home)
        .putExtra(EXTRA_CLUB_AWAY, away);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match_progress);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      home = intent.getParcelableExtra(EXTRA_CLUB_HOME);
      away = intent.getParcelableExtra(EXTRA_CLUB_AWAY);
    }

    loadClubs(home.id(), away.id());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    stopTimer();
  }

  private void loadClubs(int homeId, int awayId) {
    service.club(homeId)
        .compose(this.<Club>applyTransformations())
        .mergeWith(service.club(awayId).compose(this.<Club>applyTransformations()))
        .subscribe(new SimpleResponseObserver<Club>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(MatchProgressActivity.this, "Failed to load club",
                Toast.LENGTH_SHORT).show();
            Log.w(TAG, e);
          }

          @Override public void onNext(Club response) {
            fillClubInfos(response);
          }

          @Override public void onCompleted() {
            statistics = new DefaultMatchStatistics(home, away,
                new Well19937c(), MatchStatistics.GOALS_DISTRIBUTION);

            String winner = !statistics.isDraw() ? statistics.winner().abbrev_name() : "draw";
            finalScoreMessage =
                winner + " is the winner. Final score " + statistics.finalScore() + ".";
            Log.d(TAG, finalScoreMessage);

            startTimer();
          }
        });
  }

  private void fillClubInfos(Club club) {
    ImageView imgView;
    TextView txtView;
    if (club.equals(home)) {
      imgView = imgTeamHome;
      txtView = txtTeamHome;
    } else {
      imgView = imgTeamAway;
      txtView = txtTeamAway;
    }
    Picasso.with(MatchProgressActivity.this)
        .load(club.large_image())
        .into(imgView);

    txtView.setText(club.abbrev_name().substring(0, 3).toUpperCase());
  }

  private void stopTimer() {
    subscriptions.clear();
    isRunning = false;
  }

  private void startTimer() {
    subscriptions.add(statistics.eventsObservable(elapsedMinutes)
        .map(new Func1<MatchEvent, Goal>() {
          @Override public Goal call(MatchEvent matchEvent) {
            return (Goal) matchEvent;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Goal>() {
          @Override public void call(Goal goal) {
            TextView txtScore = goal.club().equals(home) ? txtTeamHomeGoals : txtTeamAwayGoals;
            int currGoals = Integer.parseInt(txtScore.getText().toString());
            txtScore.setText(String.valueOf(++currGoals));
            appendEvent(goal.time() + "' " + goal.club().abbrev_name() + " goal.");
          }
        }));

    subscriptions.add(Observable.interval(0, 1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Long>() {
          @Override public void call(Long _) {
            elapsedMinutes++;
            fractionView.setFraction(elapsedMinutes % 45, 60);
            if (elapsedMinutes == 45) {
              appendEvent(strEndOfFirstHalf);
            } else if (elapsedMinutes == 90) {
              stopTimer();
              appendEvent(strEndOfMatch);
              appendEvent(finalScoreMessage);
              playPauseButton.setVisibility(View.GONE);
              doneButton.setVisibility(View.VISIBLE);
              fractionView.setFraction(45, 60);
            }
          }
        }));
    isRunning = true;
  }

  private void appendEvent(String text) {
    txtMatchEvents.setText(text + "\n" + txtMatchEvents.getText());
  }

  @OnClick(R.id.fab_play_pause) public void onClickPause() {
    if (isRunning) {
      stopTimer();
      Toast.makeText(this, R.string.match_paused, Toast.LENGTH_SHORT).show();
      playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_48dp);
    } else {
      startTimer();
      Toast.makeText(this, R.string.match_resumed, Toast.LENGTH_SHORT).show();
      playPauseButton.setImageResource(R.drawable.ic_pause_white_48dp);
    }
  }

  @OnClick(R.id.fab_done) public void onClickDone() {
    League league = preferences.retrieveUserLeague();
    Club club = preferences.retrieveUserClub();
    startActivity(LeagueDetailsActivity.newIntent(this, league, club));
  }
}
