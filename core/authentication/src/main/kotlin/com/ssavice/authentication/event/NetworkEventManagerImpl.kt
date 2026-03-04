package com.ssavice.authentication.event

import android.util.Log
import com.ssavice.network.NetworkEvent
import com.ssavice.network.NetworkEventManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkEventManagerImpl
@Inject
constructor() : NetworkEventManager {
    private val _event = MutableSharedFlow<NetworkEvent>(10)
    override val event = _event.asSharedFlow()
    override fun tryEmit(event: NetworkEvent) {
        Log.d(TAG, "tryEmit: $event")
        _event.tryEmit(event)
    }

    override suspend fun emit(event: NetworkEvent) {
        Log.d(TAG, "emit: $event")
        _event.emit(event)
    }

    companion object {
        const val TAG = "NetworkEventManager"
    }
}
