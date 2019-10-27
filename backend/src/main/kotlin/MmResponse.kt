import io.ktor.http.HttpStatusCode

sealed class MmResponse(
        val statusCode: HttpStatusCode,
        val message: String
) {
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

    data class SuccessData(
            val data: Map<String, Any>
    ) : MmResponse(
            statusCode = HttpStatusCode.OK,
            message = "Response success!"
    )
}
