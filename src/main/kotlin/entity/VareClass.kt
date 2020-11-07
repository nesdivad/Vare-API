package h577870.entity

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


//Table-objekt
object Vare: Table() {
    val ean: Column<Long> = long("ean")
    val navn: Column<String> = varchar("navn", 50)
    var pris: Column<Int> = integer("pris")
    val beskrivelse: Column<String> = varchar("beskrivelse", 50)
    val plu: Column<Int> = integer("plu")
    var sortimentskode: Column<String> = varchar("sortimentskode", 5)
    val kategori: Column<String> = varchar("kategori", 20)
}

/**
 * @author Kristoffer Davidsen
 * @param ean -> Artikkelnummer for Vare.
 * @param navn -> Navn på vare.
 * @param pris -> Pris på vare.
 * @param beskrivelse -> Beskrivelse av varen.
 * @param plu -> Alternativ til ean, kan være null.
 * @param sortimentskode -> Sortimentskode for vare, bestemmes av varens popularitet osv.
 * @param kategori -> Kategorien varen tilhører.
 */

@Serializable
data class VareClass(
    val ean: Long,
    val navn: String,
    var pris: Int,
    val beskrivelse: String,
    val plu: Int?,
    var sortimentskode: String,
    val kategori: String
)

