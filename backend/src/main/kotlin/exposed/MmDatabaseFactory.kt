package exposed

import MmHoconConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import exposed.dsl.MmSessions
import exposed.dsl.MmUsers
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Database configuration.
 */
@KtorExperimentalAPI
object MmDatabaseFactory {

    /**
     * Connects to database and creates tables.
     */
    fun init() {
        Database.connect(hikari())
        transaction {
            addLogger(Slf4jSqlDebugLogger) // use sql with Slf4j logger
            create(MmUsers, MmSessions) // create tables
        }
    }

    /**
     * [HikariDataSource] configuration to connect to postgresql.
     */
    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = MmHoconConfig.dbUrl
            username = MmHoconConfig.dbUser
            password = MmHoconConfig.dbPassword
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }
}