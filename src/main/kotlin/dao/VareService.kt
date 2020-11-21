package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.Vare
import h577870.entity.VareClass
import io.ktor.util.*
import org.jetbrains.exposed.sql.*

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

    suspend fun oppdaterVare(nyvare: VareClass) : Int? = dbQuery {
        Vare.update({ Vare.ean eq nyvare.ean }) {
            it[pris] = nyvare.pris
            it[sortimentskode] = nyvare.sortimentskode
        }
    }

    suspend fun leggTilVare(nyvare: VareClass) : Unit = dbQuery {
        Vare.insert {
            it[ean] = nyvare.ean
            it[navn] = nyvare.navn
            it[pris] = nyvare.pris
            it[sortimentskode] = nyvare.sortimentskode
            it[kategori] = nyvare.kategori
            if (nyvare.plu != null) it[plu] = nyvare.plu
            it[beskrivelse] = nyvare.beskrivelse
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