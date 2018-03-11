package com.elifut.services

import android.app.Activity

class GoogleApiConnectionHandlerFactory {
  companion object {
    val ENABLED = false

    fun newInstance(activity: Activity): GoogleApiConnectionHandler = if (ENABLED) {
      RealGoogleApiConnectionHandler(activity)
    } else {
      NoOpGoogleApiConnectionHandler()
    }
  }
}
