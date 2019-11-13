package exposed

import exposed.dsl.MmUsers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Object to setup the database configuration.
 * Connects to the database and creates tables.
 */
object MmDatabase {
    fun setup() {
        Database.connect("jdbc:h2:./data/test", driver = "org.h2.Driver")
        transaction {
            // use sql with Slf4j logger
            addLogger(Slf4jSqlDebugLogger)

            // create tables
            create(MmUsers)
        }
    }
}