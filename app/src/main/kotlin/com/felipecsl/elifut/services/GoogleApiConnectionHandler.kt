package com.felipecsl.elifut.services

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.felipecsl.elifut.R
import com.felipecsl.elifut.models.GoogleApiConnectionResult
import com.felipecsl.elifut.util.BaseGameUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.Player
import rx.Single
import rx.subjects.PublishSubject

class GoogleApiConnectionHandler(private val activity: Activity) :
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
  private val googleApiClient: GoogleApiClient
  private var resolvingConnectionFailure = false
  private var signInClicked: Boolean = false
  private var autoStartSignInFlow = true
  private val resultSubject: PublishSubject<GoogleApiConnectionResult> =
      PublishSubject.create<GoogleApiConnectionResult>()

  init {
    // Create the Google API Client with access to Games
    googleApiClient = GoogleApiClient.Builder(activity)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Games.API)
        .addScope(Games.SCOPE_GAMES)
        .build()
  }

  override fun onConnected(connectionHint: Bundle?) {
    Log.d(TAG, "onConnected(): connected to Google APIs")
    val player: Player? = Games.Players.getCurrentPlayer(googleApiClient)
    resultSubject.onNext(GoogleApiConnectionResult(connectionHint, player, googleApiClient))
    resultSubject.onCompleted()
  }

  override fun onConnectionSuspended(i: Int) {
    Log.d(TAG, "onConnectionSuspended(): attempting to connect")
    googleApiClient.connect()
  }

  fun onStart() {
    Log.d(TAG, "onStart(): connecting")
    googleApiClient.connect()
  }

  fun onStop() {
    Log.d(TAG, "onStop(): disconnecting")
    if (googleApiClient.isConnected) {
      googleApiClient.disconnect()
    }
  }

  fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    if (requestCode == RC_SIGN_IN) {
      signInClicked = false
      resolvingConnectionFailure = false
      if (resultCode == Activity.RESULT_OK) {
        googleApiClient.connect()
      } else {
        BaseGameUtils.showActivityResultError(activity, requestCode, resultCode,
            R.string.signin_other_error)
      }
    }
  }

  fun result(): Single<GoogleApiConnectionResult> {
    return resultSubject.toSingle()
  }

  fun connect() {
    signInClicked = true
    googleApiClient.connect()
  }

  override fun onConnectionFailed(connectionResult: ConnectionResult) {
    Log.d(TAG, "onConnectionFailed(): attempting to resolve")
    if (resolvingConnectionFailure) {
      Log.d(TAG, "onConnectionFailed(): already resolving")
      return
    }

    if (signInClicked || autoStartSignInFlow) {
      autoStartSignInFlow = false
      signInClicked = false
      resolvingConnectionFailure = true
      if (!BaseGameUtils.resolveConnectionFailure(activity, googleApiClient, connectionResult,
          RC_SIGN_IN, activity.getString(R.string.signin_other_error))) {
        resolvingConnectionFailure = false
      }
    }

    resultSubject.onError(RuntimeException(connectionResult.errorMessage))
  }

  companion object {
    private val TAG = "GoogleApiClientCallback"
    // request codes we use when invoking an external activity
    private val RC_SIGN_IN = 9001
  }
}
