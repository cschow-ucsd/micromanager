import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.main() {
    routing {
        get("/") {
            call.respondText("<h1>Hello, world!</h1>", ContentType.Text.Html)
        }
    }
}