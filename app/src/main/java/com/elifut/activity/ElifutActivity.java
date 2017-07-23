package com.elifut.activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.elifut.ElifutApplication;
import com.elifut.ElifutComponent;
import com.elifut.Util;
import com.elifut.models.Club;
import com.elifut.preferences.LeagueDetails;
import com.elifut.preferences.UserPreferences;
import com.elifut.services.ElifutService;
import com.elifut.util.ColorUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import icepick.Icepick;
import retrofit2.Response;
import rx.Observable;

public abstract class ElifutActivity extends AppCompatActivity  {
  @Inject ElifutService service;
  @Inject UserPreferences userPreferences;
  @Inject LeagueDetails leagueDetails;
  @Inject Tracker tracker;

  protected ElifutApplication getElifutApp() {
    return (ElifutApplication) getApplication();
  }

  protected ElifutComponent daggerComponent() {
    return getElifutApp().component();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    daggerComponent().inject(this);
    Icepick.restoreInstanceState(this, savedInstanceState);
    String screenName = getClass().getSimpleName();
    Log.i(screenName, "Setting screen name: " + screenName);
    tracker.setScreenName(screenName);
    tracker.send(new HitBuilders.ScreenViewBuilder().build());
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  protected void colorizeToolbar(Club club, Toolbar toolbar, final int colorPrimary) {
    Picasso.with(this)
        .load(club.large_image())
        .into(new SimpleTarget() {
          @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Palette.from(bitmap).generate(palette ->
                ColorUtils.colorizeHeader(ElifutActivity.this, toolbar,
                    palette.getDarkVibrantColor(colorPrimary))
            );
          }
        });
  }

  public <T> Observable.Transformer<Response<T>, T> applyTransformations() {
    return Util.apiObservableTransformer();
  }
}
