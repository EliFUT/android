package com.felipecsl.elifut.models

import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Player

class GoogleApiConnectionResult(
    val connection: Bundle?, val player: Player?, val apiClient: GoogleApiClient) {
}