package util

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

val mmDotenv: Dotenv by lazy {
    dotenv {
        directory = "./backend/src/main/resources"
    }
}

fun Dotenv.requireOrMissing(
        name: String
) = requireNotNull(this[name]) { "$name is missing, please add to .env file at backend/src/main/resources folder. Working directory: ${System.getProperty("user.dir")}" }

val Dotenv.BACKEND_OAUTH_CLIENT_ID: String
    get() = requireOrMissing(::BACKEND_OAUTH_CLIENT_ID.name)

val Dotenv.BACKEND_OAUTH_CLIENT_SECRET: String
    get() = requireOrMissing(::BACKEND_OAUTH_CLIENT_SECRET.name)

val Dotenv.BACKEND_TRANSFORMER_HEX: String
    get() = requireOrMissing(::BACKEND_TRANSFORMER_HEX.name)