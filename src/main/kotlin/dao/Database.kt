package h577870.dao

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction


@KtorExperimentalAPI
object DatabaseFactory {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = appConfig.property("db.jdbcUrl").getString()
    private val dbUser = appConfig.property("db.dbUser").getString()
    private val dbPassword = appConfig.property("db.dbPassword").getString()


    /*
    Initialize database connection using a HikariDataSource.

     */
    fun init() {
        Database.connect(hikari())
        val flyway = Flyway.configure().dataSource(
            "$dbUrl?currentSchema=varer",
            dbUser, dbPassword).baselineOnMigrate(true).load()
        flyway.migrate()
    }

    private fun hikari(): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "org.postgresql.Driver"
        hikariConfig.jdbcUrl = "$dbUrl?currentSchema=varer"
        hikariConfig.username = dbUser
        hikariConfig.password = dbPassword
        hikariConfig.maximumPoolSize = 2
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}