package api

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.jetbrains.exposed.exceptions.EntityNotFoundException
import util.logger

fun StatusPages.Configuration.mmStatusPagesConfiguration() {
    exception<MmAuthException> {
        call.logger.debug(it.message)
        call.respond(HttpStatusCode.Unauthorized, it.jsonMessage)
    }
    exception<EntityNotFoundException> {
        call.logger.debug(it.message)
        call.respond(HttpStatusCode.InternalServerError)
    }
    exception<Throwable> {
        call.logger.debug(it.message)
        // uncaught exceptions, so throw InternalServerError
        call.respond(HttpStatusCode.InternalServerError)
    }
}