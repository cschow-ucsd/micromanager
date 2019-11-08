package util

import api.MmSession
import io.ktor.application.ApplicationCall
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.slf4j.Logger

val ApplicationCall.logger: Logger
    get() = application.environment.log

var ApplicationCall.mmSession: MmSession?
    get() = sessions.get()
    set(value) = sessions.set(value)