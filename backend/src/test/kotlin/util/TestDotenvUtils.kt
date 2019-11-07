package util

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import requireOrMissing

val testMmDotenv: Dotenv by lazy {
    dotenv {
        directory = "./src/main/resources"
    }
}

val Dotenv.CLIENT_SERVER_AUTH_TOKEN_TEST: String
    get() = requireOrMissing(::CLIENT_SERVER_AUTH_TOKEN_TEST.name)

val Dotenv.CLIENT_EMAIL_TEST: String
    get() = requireOrMissing(::CLIENT_EMAIL_TEST.name)