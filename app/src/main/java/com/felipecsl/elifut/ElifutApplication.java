package com.felipecsl.elifut;

import android.app.Application;

public class ElifutApplication extends Application {

  private ElifutComponent component;

  @Override public void onCreate() {
    super.onCreate();

    component = ElifutComponent.Initializer.init(this);
  }

  public ElifutComponent component() {
    return component;
  }
}
