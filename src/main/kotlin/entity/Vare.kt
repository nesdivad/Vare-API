package h577870.entity

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlinx.serialization.*


//Table-objekt
object Varer: Table() {
    val ean: Column<Long> = long("ean")
    val navn: Column<String> = varchar("navn", 50)
    var pris: Column<Int> = integer("pris")
    val beskrivelse: Column<String> = varchar("beskrivelse", 50)
    val plu: Column<Int> = integer("plu")
    var sortimentskode: Column<String> = varchar("sortimentskode", 5)
    val kategori: Column<String> = varchar("kategori", 20)
}

@Serializable
data class Vare(
    val ean: Long,
    val navn: String,
    var pris: Int,
    val beskrivelse: String,
    val plu: Int?,
    var sortimentskode: String,
    val kategori: String
)