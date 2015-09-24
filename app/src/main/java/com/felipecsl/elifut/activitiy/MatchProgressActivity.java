package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import retrofit.Response;
import rx.android.schedulers.AndroidSchedulers;

public class MatchProgressActivity extends ElifutActivity {
  private static final String EXTRA_CLUB_HOME = "EXTRA_CLUB_HOME";
  private static final String EXTRA_CLUB_AWAY = "EXTRA_CLUB_AWAY";
  private static final String TAG = MatchProgressActivity.class.getSimpleName();

  @Bind(R.id.img_team_home) ImageView imgTeamHome;
  @Bind(R.id.img_team_away) ImageView imgTeamAway;
  @Bind(R.id.txt_team_home) TextView txtTeamHome;
  @Bind(R.id.txt_team_away) TextView txtTeamAway;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @State Club home;
  @State Club away;

  public static Intent newIntent(Context context, Club home, Club away) {
    return new Intent(context, MatchProgressActivity.class)
        .putExtra(EXTRA_CLUB_HOME, home)
        .putExtra(EXTRA_CLUB_AWAY, away);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match_progress);
    ButterKnife.bind(this);
    Icepick.restoreInstanceState(this, savedInstanceState);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      home = intent.getParcelableExtra(EXTRA_CLUB_HOME);
      away = intent.getParcelableExtra(EXTRA_CLUB_AWAY);
    }

    loadClub(home.id());
    loadClub(away.id());
  }

  private void loadClub(int id) {
    service.club(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SimpleResponseObserver<Club>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(MatchProgressActivity.this, "Failed to load club",
                Toast.LENGTH_SHORT).show();
            Log.w(TAG, e);
          }

          @Override public void onNext(Response<Club> response) {
            if (response.isSuccess()) {
              fillClubInfos(response.body());
            } else {
              Toast.makeText(MatchProgressActivity.this, "Failed to load club",
                  Toast.LENGTH_SHORT).show();
            }
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

  @OnClick(R.id.fab) public void onClickPause() {

  }
}
