package h577870.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object Bruker : Table() {
    val brukernavn: Column<String> = varchar("brukernavn", length = 50)
    val passord: Column<String> = varchar("passord", length = 256)
    val bruker_key = PrimaryKey(brukernavn, name = "bruker_pkey")
}

@Serializable
data class BrukerClass(
        val brukernavn: String,
        //TODO: Serialisering på passord. Bcrypt.
        val passord: String
) {

    companion object {
        val bcrypt = BCryptPasswordEncoder()
        fun kontrollerBruker(body: BrukerClass, dbbruker: BrukerClass): Boolean {
            return bcrypt.matches(body.passord, dbbruker.passord)
        }
    }
}