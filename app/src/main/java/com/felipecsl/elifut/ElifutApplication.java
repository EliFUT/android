package com.felipecsl.elifut;

import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class ElifutApplication extends MultiDexApplication {
  protected ElifutComponent component;

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    component = ElifutComponent.Initializer.init(this);
    StethoInitializer.initialize(this);
  }

  public ElifutComponent component() {
    return component;
  }
}
