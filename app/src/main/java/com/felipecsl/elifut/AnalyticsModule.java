package com.felipecsl.elifut;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {
  private final Context context;

  public AnalyticsModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton public Tracker getDefaultTracker() {
    GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
    // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
    return analytics.newTracker(R.xml.global_tracker);
  }
}
