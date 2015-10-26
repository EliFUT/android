package com.felipecsl.elifut;

import android.support.multidex.MultiDexApplication;

public class ElifutApplication extends MultiDexApplication {
  private ElifutComponent component;

  @Override public void onCreate() {
    super.onCreate();
    component = ElifutComponent.Initializer.init(this);
    StethoInitializer.initialize(this);
  }

  public ElifutComponent component() {
    return component;
  }
}
