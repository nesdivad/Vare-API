package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.VareClass
import h577870.entity.Vare
import io.ktor.util.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

/*
Utfører queries på databasen.
 */

@KtorExperimentalAPI
class VareService {

    suspend fun hentAlleVarer(): List<VareClass> = dbQuery {
        Vare.selectAll().map { convertVare(it) }
    }

    suspend fun hentVareMedEan(ean: String) : VareClass? = dbQuery {
        Vare.select {
            (Vare.ean eq ean.toLong())
        }.mapNotNull { convertVare(it) }
            .singleOrNull()
    }

    suspend fun hentVareMedNavn(navn: String) : VareClass? = dbQuery {
        Vare.select {
            (Vare.navn eq navn)
        }.mapNotNull { convertVare(it) }
            .singleOrNull()
    }

    suspend fun oppdaterPris(ean: String, pris: String) : Int = dbQuery {
        Vare.update({ Vare.ean eq ean.toLong() }) {
            it[Vare.pris] = pris.toInt()
        }
    }

    private fun convertVare(row: ResultRow): VareClass =
        VareClass(
            ean = row[Vare.ean],
            navn = row[Vare.navn],
            beskrivelse = row[Vare.beskrivelse],
            plu = row[Vare.plu],
            kategori = row[Vare.kategori],
            pris = row[Vare.pris],
            sortimentskode = row[Vare.sortimentskode]
        )
}