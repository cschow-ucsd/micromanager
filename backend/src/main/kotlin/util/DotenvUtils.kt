package util

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

/**
 * Loads secret keys from .env file.
 * Avoids hardcoding them and leaking the keys.
 */
val mmDotenv: Dotenv by lazy {
    dotenv {
        directory = "./backend/src/main/resources"
    }
}

/**
 * Helper function to obtain secret keys from .env file.
 * @param name of variable in .env file.
 */
fun Dotenv.requireOrMissing(
        name: String
) = requireNotNull(this[name]) { "$name is missing, please add to .env file at backend/src/main/resources folder. Working directory: ${System.getProperty("user.dir")}" }

val Dotenv.BACKEND_OAUTH_CLIENT_ID: String
    get() = requireOrMissing(::BACKEND_OAUTH_CLIENT_ID.name)

val Dotenv.BACKEND_OAUTH_CLIENT_SECRET: String
    get() = requireOrMissing(::BACKEND_OAUTH_CLIENT_SECRET.name)

val Dotenv.BACKEND_TRANSFORMER_HEX: String
    get() = requireOrMissing(::BACKEND_TRANSFORMER_HEX.name)