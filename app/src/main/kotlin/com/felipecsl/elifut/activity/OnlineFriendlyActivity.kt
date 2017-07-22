package com.felipecsl.elifut.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.felipecsl.elifut.R
import com.felipecsl.elifut.models.GoogleApiConnectionResult
import com.felipecsl.elifut.services.GoogleApiConnectionHandler
import com.felipecsl.elifut.services.GoogleApiConnectionHandlerFactory
import com.felipecsl.elifut.services.GoogleApiGameRoomHandler
import rx.SingleSubscriber
import rx.subscriptions.CompositeSubscription

class OnlineFriendlyActivity : NavigationActivity() {
  @BindView(R.id.layout_logs) private lateinit var logsLayout: TextView

  private lateinit var googleApiConnectionHandler: GoogleApiConnectionHandler
  private lateinit var googleApiGameRoomHandler: GoogleApiGameRoomHandler
  private val subscriptions = CompositeSubscription()
  private val playerSubscriber = object : SingleSubscriber<GoogleApiConnectionResult>() {
    override fun onSuccess(result: GoogleApiConnectionResult) {
      Log.d(TAG, "onConnected(). Sign in successful!")
      createRoom(result)
    }

    override fun onError(error: Throwable) {
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ButterKnife.bind(this)
    val actionBar = checkNotNull(supportActionBar)
    actionBar.setTitle(R.string.online_friendly)
    googleApiConnectionHandler = GoogleApiConnectionHandlerFactory.newInstance(this)
    subscriptions.add(googleApiConnectionHandler.result().subscribe(playerSubscriber))
    googleApiConnectionHandler.connect()
  }

  override fun layoutId(): Int {
    return R.layout.activity_online_friendly
  }

  override fun onStart() {
    super.onStart()
    googleApiConnectionHandler.onStart()
  }

  override fun onStop() {
    super.onStop()
    googleApiConnectionHandler.onStop()
  }

  private fun createRoom(result: GoogleApiConnectionResult) {
    googleApiGameRoomHandler = GoogleApiGameRoomHandler(this, result)
    googleApiGameRoomHandler.createRoom()
  }

  override fun onActivityResult(requestCode: Int, responseCode: Int, intent: Intent) {
    super.onActivityResult(requestCode, responseCode, intent)
    googleApiConnectionHandler.onActivityResult(requestCode, responseCode)
    googleApiGameRoomHandler.onActivityResult(requestCode, responseCode, intent)
  }

  override fun onDestroy() {
    super.onDestroy()
    subscriptions.unsubscribe()
  }

  companion object Factory {
    private val TAG = "OnlineFriendlyActivity"
    fun newIntent(context: Context) = Intent(context, OnlineFriendlyActivity::class.java)
  }
}
