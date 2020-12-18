package h577870.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Bruker : Table() {
    val brukernavn: Column<String> = varchar("brukernavn", length = 50)
    val passord: Column<String> = varchar("passord", length = 256)
    val butikknr: Column<Int> = integer("butikknr")
    val privilege: Column<Boolean> = bool("privilege")
    val bruker_key = PrimaryKey(brukernavn, name = "bruker_pkey")
}

@Serializable
data class BrukerClass(
        val brukernavn: String,
        val passord: String,
        val butikknr: Int,
        val privilege: Boolean
)