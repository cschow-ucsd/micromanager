package api

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.jetbrains.exposed.exceptions.EntityNotFoundException
import util.logger

/**
 * [StatusPages] configuration.
 * Handles exceptions thrown in the receive/response pipeline.
 * Logs exception messages and responds corresponding status code.
 */
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