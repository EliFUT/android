package com.elifut;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class ElifutApplication extends Application {
  protected ElifutComponent component;

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.installMultiDex(this);
  }

  @Override public void onCreate() {
    super.onCreate();
    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }
    component = ElifutComponent.Initializer.init(this);
    StethoInitializer.initialize(this);
  }

  public ElifutComponent component() {
    return component;
  }
}
