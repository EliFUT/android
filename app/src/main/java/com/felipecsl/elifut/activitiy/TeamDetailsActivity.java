package com.felipecsl.elifut.activitiy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.services.ElifutService;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit.Response;
import rx.android.schedulers.AndroidSchedulers;

public class TeamDetailsActivity extends AppCompatActivity {
  private static final String EXTRA_COUNTRY = "EXTRA_COUNTRY";
  private static final String EXTRA_NAME = "EXTRA_NAME";
  private static final String TAG = TeamDetailsActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.backdrop) ImageView backdrop;

  @State Nation nation;
  @State String coachName;

  @Inject ElifutService service;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team_details);

    Icepick.restoreInstanceState(this, savedInstanceState);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      coachName = intent.getStringExtra(EXTRA_NAME);
      nation = intent.getParcelableExtra(EXTRA_COUNTRY);
    }

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    ElifutApplication application = (ElifutApplication) getApplication();
    application.component().inject(this);

    service.randomClub(nation.id())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SimpleResponseObserver<Club>() {
          @Override public void onError(Throwable e) {
            Toast.makeText(TeamDetailsActivity.this, "Failed to load a club", Toast.LENGTH_SHORT)
                .show();
            Log.w(TAG, e);
          }

          @Override public void onNext(Response<Club> response) {
            Club club = response.body();
            collapsingToolbar.setTitle(club.name());
            loadBackdrop(club);
          }
        });
  }

  private void loadBackdrop(Club club) {
    Picasso.with(TeamDetailsActivity.this)
        .load(club.remoteImage())
        .into(new SimpleTarget() {
          @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            backdrop.setImageBitmap(bitmap);
            loadPalette(bitmap);
          }
        });
  }

  private void loadPalette(Bitmap bitmap) {
    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
      public void onGenerated(Palette palette) {
        int defaultColor = getResources().getColor(R.color.color_primary);
        int color = palette.getMutedColor(defaultColor);
        collapsingToolbar.setContentScrimColor(color);
        collapsingToolbar.setBackgroundColor(color);
      }
    });
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  public static Intent newIntent(Context context, Nation nation, String name) {
    return new Intent(context, TeamDetailsActivity.class)
        .putExtra(EXTRA_COUNTRY, nation)
        .putExtra(EXTRA_NAME, name);
  }
}
