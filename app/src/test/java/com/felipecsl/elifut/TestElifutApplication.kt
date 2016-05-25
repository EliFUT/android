package com.felipecsl.elifut

class TestElifutApplication : ElifutApplication() {

  override fun onCreate() {
    // Never call super() so we don't initialize Stetho
    component = TestElifutComponent.Initializer.init(this)
  }

  fun testComponent(): TestElifutComponent {
    return component as TestElifutComponent
  }
}
