package com.ssavice.authentication.event

import com.ssavice.network.AuthEvent
import com.ssavice.network.AuthEventManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEventManagerImpl
    @Inject
    constructor() : AuthEventManager {
        private val _event = MutableSharedFlow<AuthEvent>()
        override val event = _event.asSharedFlow()

        override suspend fun emit(event: AuthEvent) {
            _event.emit(event)
        }
    }
