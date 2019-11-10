package util

import io.ktor.application.ApplicationCall
import io.ktor.sessions.clear
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.slf4j.Logger
import sessions.MmSessionData

val ApplicationCall.logger: Logger
    get() = application.environment.log

var ApplicationCall.mmSessionData: MmSessionData?
    get() = sessions.get()
    set(value) {
        sessions.clear<MmSessionData>()
        sessions.set(value)
    }