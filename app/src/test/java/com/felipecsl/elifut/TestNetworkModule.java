package com.felipecsl.elifut;

import android.content.Context;

import dagger.Module;

@Module
public class TestNetworkModule extends NetworkModule {
  public TestNetworkModule(Context context) {
    super(context);
  }
}
