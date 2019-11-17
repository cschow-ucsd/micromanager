package util

import api.MmSession
import io.ktor.application.ApplicationCall
import io.ktor.sessions.clear
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.slf4j.Logger

/**
 * Property to access the logger of [ApplicationCall] easier.
 */
val ApplicationCall.logger: Logger
    get() = application.environment.log

/**
 * Property to access [MmSession] of [ApplicationCall] easier.
 * Can get and set this mutable value.
 */
var ApplicationCall.mmSession: MmSession?
    get() = sessions.get()
    set(value) {
        sessions.clear<MmSession>()
        sessions.set(value)
    }