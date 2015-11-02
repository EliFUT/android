package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.elifut.DefaultMatchStatistics;
import com.felipecsl.elifut.MatchStatistics;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.MatchEvent;
import com.felipecsl.elifut.widget.FractionView;
import com.squareup.picasso.Picasso;

import org.apache.commons.math3.random.Well19937c;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

public class MatchProgressActivity extends ElifutActivity {
  private static final String EXTRA_CLUB_HOME = "EXTRA_CLUB_HOME";
  private static final String EXTRA_CLUB_AWAY = "EXTRA_CLUB_AWAY";
  private static final String TAG = MatchProgressActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.img_team_home) ImageView imgTeamHome;
  @Bind(R.id.img_team_away) ImageView imgTeamAway;
  @Bind(R.id.txt_team_home) TextView txtTeamHome;
  @Bind(R.id.txt_team_away) TextView txtTeamAway;
  @Bind(R.id.txt_team_home_goals) TextView txtTeamHomeGoals;
  @Bind(R.id.txt_team_away_goals) TextView txtTeamAwayGoals;
  @Bind(R.id.fractionView) FractionView fractionView;
  @Bind(R.id.fab) FloatingActionButton playPauseButton;

  @State Club home;
  @State Club away;
  @State boolean isRunning;
  @State int elapsedMinutes;
  @State boolean isSecondHalf;
  @State DefaultMatchStatistics statistics;

  private final Runnable timerRunnable = new Runnable() {
    @Override public void run() {
      if (isFinishing())
        return;

      if (elapsedMinutes < 45) {
        advanceTimer();
      } else if (!isSecondHalf) {
        Toast.makeText(MatchProgressActivity.this,
            getString(R.string.end_first_half), Toast.LENGTH_SHORT).show();
        isSecondHalf = true;
        elapsedMinutes = 0;
        advanceTimer();
      }
    }
  };

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

    txtView.setText(club.name().substring(0, 3).toUpperCase());
  }

  private void stopTimer() {
    fractionView.removeCallbacks(timerRunnable);
    isRunning = false;
  }

  private void startTimer() {
    fractionView.postDelayed(timerRunnable, DateUtils.SECOND_IN_MILLIS);
    isRunning = true;
  }

  private void advanceTimer() {
    Set<MatchEvent> events = statistics.eventsAtTime(elapsedMinutes);
    for (MatchEvent event : events) {
      if (event instanceof Goal) {
        addGoal(((Goal) event).club().equals(home) ? txtTeamHomeGoals : txtTeamAwayGoals);
      }
    }
    fractionView.setFraction(++elapsedMinutes, 60);
    fractionView.postDelayed(timerRunnable, DateUtils.SECOND_IN_MILLIS);
  }

  private void addGoal(TextView txtScore) {
    int currGoals = Integer.parseInt(txtScore.getText().toString());
    txtScore.setText(String.valueOf(++currGoals));
  }

  @OnClick(R.id.fab) public void onClickPause() {
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
}
