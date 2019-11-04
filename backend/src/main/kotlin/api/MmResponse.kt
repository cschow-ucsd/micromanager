package api

import io.ktor.http.HttpStatusCode

sealed class MmResponse(
        val statusCode: HttpStatusCode,
        val message: String
) {
    object LoginSuccess : MmResponse(
            statusCode = HttpStatusCode.OK,
            message = "api.Login success!"
    )

    object LoginFailed : MmResponse(
            statusCode = HttpStatusCode.Unauthorized,
            message = "api.Login failed."
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

    data class ApiToken(
            val token: String
    ) : MmResponse(
            statusCode = HttpStatusCode.OK,
            message = "API token."
    )
}
