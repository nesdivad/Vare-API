package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.Vare
import h577870.entity.VareClass
import h577870.entity.VareEgenskaper
import h577870.entity.VareEgenskaperClass
import io.ktor.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchUpdateStatement

@KtorExperimentalAPI
class VareEService {

    suspend fun leggTilEgenskaper(liste: List<VareEgenskaperClass>) = dbQuery {
        VareEgenskaper.batchInsert(
            liste
        ) { vareEgenskaperClass ->
            this[VareEgenskaper.ean] = vareEgenskaperClass.ean
            this[VareEgenskaper.beholdning] = vareEgenskaperClass.beholdning
            this[VareEgenskaper.dekningsperiode] = vareEgenskaperClass.dekningsperiode
            this[VareEgenskaper.prestasjonslager] = vareEgenskaperClass.prestasjonslager
            this[VareEgenskaper.snittsalg] = vareEgenskaperClass.snittsalg
        }
    }
    suspend fun hentMedEan(ean: Long) = dbQuery {
        VareEgenskaper.select {
            (VareEgenskaper.ean eq ean)
        }.mapNotNull {
            convert(it)
        }.singleOrNull()
    }

    suspend fun hentAlle() = dbQuery {
        VareEgenskaper.selectAll().mapNotNull { convert(it) }.toSet()
    }

    suspend fun oppdaterMedEan(ean: Long, newValue: Double) = dbQuery {
        VareEgenskaper.update({VareEgenskaper.ean eq ean}) {
            with(SqlExpressionBuilder) {
                it.update(beholdning, beholdning - newValue)
            }
        }
    }

    suspend fun leggTilEgenskap(vareEgenskaperClass: VareEgenskaperClass) = dbQuery {
        VareEgenskaper.insert {
            it[ean] = vareEgenskaperClass.ean
            it[beholdning] = vareEgenskaperClass.beholdning
            it[dekningsperiode] = vareEgenskaperClass.dekningsperiode
            it[prestasjonslager] = vareEgenskaperClass.prestasjonslager
            it[snittsalg] = vareEgenskaperClass.snittsalg
        }
    }

    private fun convert(res: ResultRow) : VareEgenskaperClass {
        return VareEgenskaperClass(
            ean = res[VareEgenskaper.ean],
            beholdning = res[VareEgenskaper.beholdning],
            prestasjonslager = res[VareEgenskaper.prestasjonslager],
            dekningsperiode = res[VareEgenskaper.dekningsperiode],
            snittsalg = res[VareEgenskaper.snittsalg]
        )
    }

    /*
    TODO: Batch-oppdatering av egenskaper fra salgsmelding.
     */



}