package com.elifut.services

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.elifut.R
import com.elifut.models.GoogleApiConnectionResult
import com.elifut.util.BaseGameUtils
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesActivityResultCodes
import com.google.android.gms.games.GamesStatusCodes
import com.google.android.gms.games.Player
import com.google.android.gms.games.multiplayer.Invitation
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener
import com.google.android.gms.games.multiplayer.Participant
import com.google.android.gms.games.multiplayer.realtime.*

class GoogleApiGameRoomHandler(private val activity: Activity,
    connectionResult: GoogleApiConnectionResult) : RoomUpdateListener, RoomStatusUpdateListener,
    OnInvitationReceivedListener, RealTimeMessageReceivedListener {

  private var apiClient: GoogleApiClient
  private var player: Player?
  private var opponent: Participant? = null
  private var incomingInvitation: Invitation? = null
  private var connectionHint: Bundle?
  private var roomId: String? = null
  private val basicRoomConfig: RoomConfig
  // My participant ID in the currently active game
  private var myId: String? = null

  init {
    apiClient = connectionResult.apiClient
    player = connectionResult.player
    connectionHint = connectionResult.connection
    basicRoomConfig = RoomConfig.builder(this)
        .setMessageReceivedListener(this)
        .setRoomStatusUpdateListener(this)
        .setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(1, 1, 0))
        .build()
  }

  fun createRoom() {
    listenForInvitations(connectionHint)
    Games.RealTimeMultiplayer.create(apiClient, basicRoomConfig)
    keepScreenOn()
  }

  private fun listenForInvitations(connectionHint: Bundle?) {
    // register listener so we are notified if we receive an invitation to play while we are in the
    // game
    Games.Invitations.registerInvitationListener(apiClient, this)
    if (connectionHint != null) {
      Log.d(TAG, "onConnected: connection hint provided. Checking for invite.")
      val invitation: Invitation? = connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION)
      if (invitation != null && invitation.invitationId != null) {
        // retrieve and cache the invitation ID
        Log.d(TAG, "onConnected: connection hint has a room invite!")
        acceptInviteToRoom(invitation.invitationId, apiClient)
        return
      }
    }
  }

  fun acceptInviteToRoom(invId: String, apiClient: GoogleApiClient) {
    Log.d(TAG, "Accepting invitation: " + invId)
    RoomConfig.builder(this)
        .setInvitationIdToAccept(invId)
        .setMessageReceivedListener(this)
        .setRoomStatusUpdateListener(this)
    keepScreenOn()
    Games.RealTimeMultiplayer.join(apiClient, RoomConfig.builder(this).build())
  }

  override fun onJoinedRoom(p0: Int, p1: Room?) {
  }

  override fun onRoomCreated(statusCode: Int, room: Room) {
    Log.d(TAG, "onRoomCreated($statusCode, $room)")
    if (statusCode != GamesStatusCodes.STATUS_OK) {
      Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode)
      showGameError()
      return
    }
    // save room ID so we can leave cleanly before the game starts.
    roomId = room.roomId
    showWaitingRoom(room)
  }

  // Show the waiting room UI to track the progress of other players as they enter the
  // room and get connected.
  fun showWaitingRoom(room: Room) {
    // minimum number of players required for our game
    // For simplicity, we require everyone to join the game before we start it
    // (this is signaled by Integer.MAX_VALUE).
    val intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(apiClient, room, Integer.MAX_VALUE)
    // show waiting room UI
    activity.startActivityForResult(intent, RC_WAITING_ROOM)
  }

  // Called when room is fully connected.
  override fun onRoomConnected(statusCode: Int, room: Room?) {
    Log.d(TAG, "onRoomConnected($statusCode, $room)")
    if (statusCode != GamesStatusCodes.STATUS_OK) {
      Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode)
      showGameError()
      return
    }
    updateRoom(room)
  }

  // Called when we've successfully left the room (this happens a result of voluntarily leaving
  // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
  override fun onLeftRoom(statusCode: Int, roomId: String) {
    // we have left the room; return to main screen.
    Log.d(TAG, "onLeftRoom, code " + statusCode)
  }

  override fun onRoomConnecting(room: Room?) {
    updateRoom(room)
  }

  override fun onP2PConnected(p0: String?) {
  }

  override fun onDisconnectedFromRoom(room: Room?) {
    roomId = null
    showGameError()
  }

  override fun onPeerDeclined(room: Room?, p1: MutableList<String>?) {
    updateRoom(room)
  }

  override fun onPeersConnected(room: Room?, p1: MutableList<String>?) {
    updateRoom(room)
  }

  override fun onPeerInvitedToRoom(room: Room?, p1: MutableList<String>?) {
    updateRoom(room)
  }

  override fun onPeerLeft(room: Room?, p1: MutableList<String>?) {
    updateRoom(room)
  }

  override fun onRoomAutoMatching(room: Room?) {
    updateRoom(room)
  }

  override fun onPeerJoined(room: Room?, p1: MutableList<String>?) {
    updateRoom(room)
  }

  private fun updateRoom(room: Room?) {
    if (room != null) {
      opponent = room.participants.first()
    }
    if (opponent != null) {
//      updatePeerScoresDisplay()
    }
  }

  // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
  // is connected yet).
  override fun onConnectedToRoom(room: Room) {
    Log.d(TAG, "onConnectedToRoom.")

    // get participants and my ID:
    opponent = room.participants.first()
    myId = room.getParticipantId(Games.Players.getCurrentPlayerId(apiClient))

    // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
    if (roomId == null) {
      roomId = room.roomId
    }

    Log.d(TAG, "Room ID: " + roomId)
    Log.d(TAG, "My ID: " + myId)
    Log.d(TAG, "<< CONNECTED TO ROOM>>")
  }

  override fun onPeersDisconnected(room: Room?, p1: MutableList<String>?) {
    updateRoom(room)
  }

  override fun onP2PDisconnected(p0: String?) {
  }

  override fun onInvitationRemoved(invitationId: String?) {
  }

  override fun onInvitationReceived(invitation: Invitation) {
    // We got an invitation to play a game! So, store it in incomingInvitationId
    // and show the popup on the screen.
    incomingInvitation = invitation
  }

  override fun onRealTimeMessageReceived(p0: RealTimeMessage?) {
  }

  // Sets the flag to keep this screen on. It's recommended to do that during the
  // handshake when setting up a game, because if the screen turns off, the game will be cancelled.
  private fun keepScreenOn() {
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
  }

  private fun stopKeepingScreenOn() {
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
  }

  @Suppress("UNUSED_PARAMETER")
  fun onActivityResult(requestCode: Int, responseCode: Int, intent: Intent) {
    if (requestCode == RC_WAITING_ROOM) {
      // we got the result from the "waiting room" UI.
      if (responseCode == Activity.RESULT_OK) {
        // ready to start playing
        Log.d(TAG, "Starting game (waiting room returned OK).")
        startGame()
      } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
        // player indicated that they want to leave the room
        leaveRoom()
      } else if (responseCode == Activity.RESULT_CANCELED) {
        // Dialog was cancelled (user pressed back key, for instance). In our game,
        // this means leaving the room too. In more elaborate games, this could mean
        // something else (like minimizing the waiting room UI).
        leaveRoom()
      }
    }
  }

  private fun startGame() {
  }

  fun leaveRoom() {
    Log.d(TAG, "Leaving room.")
    stopKeepingScreenOn()
    if (roomId != null) {
      Games.RealTimeMultiplayer.leave(apiClient, this, roomId)
      roomId = null
    }
  }

  // Show error message about game being cancelled and return to main screen.
  fun showGameError() {
    BaseGameUtils.makeSimpleDialog(activity, activity.getString(R.string.game_problem))
  }

  companion object Factory {
    private val TAG = "GoogleApiGameRoomHandle"
    val RC_WAITING_ROOM = 10002
  }
}