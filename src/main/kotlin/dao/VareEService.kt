package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.VareEgenskaper
import h577870.entity.VareEgenskaperClass
import io.ktor.util.*
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert

@KtorExperimentalAPI
class VareEService {

    suspend fun leggTilEgenskaper(liste: List<VareEgenskaperClass>) = dbQuery {
        VareEgenskaper.batchInsert(
            liste
        ) {
            vareEgenskaperClass ->
            this[VareEgenskaper.ean] = vareEgenskaperClass.ean
            this[VareEgenskaper.beholdning] = vareEgenskaperClass.beholdning
            this[VareEgenskaper.dekningsperiode] = vareEgenskaperClass.dekningsperiode
            this[VareEgenskaper.prestasjonslager] = vareEgenskaperClass.prestasjonslager
            this[VareEgenskaper.snittsalg] = vareEgenskaperClass.snittsalg
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

}