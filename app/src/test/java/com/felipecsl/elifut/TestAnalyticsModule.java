package com.felipecsl.elifut;

import android.content.Context;

import dagger.Module;

@Module
public class TestAnalyticsModule extends AnalyticsModule {
  public TestAnalyticsModule(Context context) {
    super(context);
  }
}
