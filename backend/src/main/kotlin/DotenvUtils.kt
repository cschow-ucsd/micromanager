import io.github.cdimascio.dotenv.Dotenv

val mmDotenv: Dotenv = Dotenv.load()

private fun Dotenv.requireOrMissing(
        name: String
) = requireNotNull(this[name]) { "$name is missing, please add to .env file at project root." }

val Dotenv.BACKEND_OAUTH_CLIENT_ID: String
    get() = requireOrMissing(::BACKEND_OAUTH_CLIENT_ID.name)

val Dotenv.BACKEND_OAUTH_CLIENT_SECRET: String
    get() = requireOrMissing(::BACKEND_OAUTH_CLIENT_SECRET.name)

val Dotenv.BACKEND_TRANSFORMER_HEX: String
    get() = requireOrMissing(::BACKEND_TRANSFORMER_HEX.name)