package com.felipecsl.elifut;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class ElifutApplication extends Application {
  private ElifutComponent component;

  @Override public void onCreate() {
    super.onCreate();
    component = ElifutComponent.Initializer.init(this);
    Stetho.initializeWithDefaults(this);
  }

  public ElifutComponent component() {
    return component;
  }
}
