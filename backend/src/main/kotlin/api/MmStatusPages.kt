package api

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.mmStatusPagesConfiguration() {
    exception<ServerAuthTokenException> {
        call.respond(HttpStatusCode.Unauthorized, it.jsonMessage)
    }
    exception<GoogleTokenException> {
        call.respond(HttpStatusCode.Unauthorized, it.jsonMessage)
    }
    exception<NoSessionException> {
        call.respond(HttpStatusCode.Unauthorized, it.jsonMessage)
    }
    exception<MmException> {
        call.respond(HttpStatusCode.BadRequest, it.jsonMessage)
    }
    exception<Throwable> {
        // uncaught exceptions, so throw InternalServerError
        call.respond(HttpStatusCode.InternalServerError)
    }
}