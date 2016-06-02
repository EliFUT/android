package com.felipecsl.elifut;

public class TestElifutApplication extends ElifutApplication {

  @Override public void onCreate() {
    // Never call super() so we don't initialize Stetho
    component = TestElifutComponent.Initializer.init(this);
  }

  public TestElifutComponent testComponent() {
    return (TestElifutComponent) component;
  }
}
