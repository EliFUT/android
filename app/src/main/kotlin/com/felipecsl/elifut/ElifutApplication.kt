package com.felipecsl.elifut

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

public open class ElifutApplication : MultiDexApplication() {
  var component: ElifutComponent? = null

  override fun onCreate() {
    super.onCreate()
    Fabric.with(this, Crashlytics())
    component = ElifutComponent.Initializer.init(this)
    StethoInitializer.initialize(this)
  }

  fun component(): ElifutComponent {
    return component!!
  }
}
