package com.felipecsl.elifut.services

import android.os.Bundle
import com.felipecsl.elifut.models.GoogleApiConnectionResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import rx.Single

class NoOpGoogleApiConnectionHandler :
    GoogleApiConnectionHandler,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
  override fun onStart() {
  }

  override fun onStop() {
  }

  override fun onActivityResult(requestCode: Int, responseCode: Int) {
    TODO("not implemented")
  }

  override fun connect() {
  }

  override fun result(): Single<GoogleApiConnectionResult> {
    return Single.error(NotImplementedError())
  }

  override fun onConnected(p0: Bundle?) {
  }

  override fun onConnectionSuspended(p0: Int) {
  }

  override fun onConnectionFailed(p0: ConnectionResult) {
  }
}
