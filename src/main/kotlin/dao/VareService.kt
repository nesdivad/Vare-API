package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.Vare
import h577870.entity.Varer
import io.ktor.util.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

@KtorExperimentalAPI
class VareService {

    suspend fun hentAlleVarer(): List<Vare> = dbQuery {
        Varer.selectAll().map { convertVare(it) }
    }

    suspend fun hentVareMedEan(ean: Long) : Vare? = dbQuery {
        Varer.select {
            (Varer.ean eq ean)
        }.mapNotNull { convertVare(it) }
            .singleOrNull()
    }

    suspend fun hentVareMedNavn(navn: String) : Vare? = dbQuery {
        Varer.select {
            (Varer.navn eq navn)
        }.mapNotNull { convertVare(it) }
            .singleOrNull()
    }

    private fun convertVare(row: ResultRow): Vare =
        Vare(
            ean = row[Varer.ean],
            navn = row[Varer.navn],
            beskrivelse = row[Varer.beskrivelse],
            plu = row[Varer.plu],
            kategori = row[Varer.kategori],
            pris = row[Varer.pris],
            sortimentskode = row[Varer.sortimentskode]
        )
}