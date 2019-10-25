import io.ktor.http.HttpStatusCode

sealed class MmResponse(
        val statusCode: HttpStatusCode,
        val message: String
) : HashMap<String, Any>() {
    init {
        super.put(::statusCode.name, statusCode)
        super.put(::message.name, message)
    }

    object LoginSuccess : MmResponse(
            statusCode = HttpStatusCode.OK,
            message = "Login success!"
    )

    object LoginFailed : MmResponse(
            statusCode = HttpStatusCode.Unauthorized,
            message = "Login failed."
    )

    object NoLogin : MmResponse(
            statusCode = HttpStatusCode.Forbidden,
            message = "No login."
    )
}
