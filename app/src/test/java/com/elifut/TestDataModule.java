package com.elifut;

import android.content.Context;

import dagger.Module;

@Module
public class TestDataModule extends DataModule {
  public TestDataModule(Context context) {
    super(context);
  }
}
