package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.Bruker
import h577870.entity.BrukerClass
import io.ktor.util.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@KtorExperimentalAPI
class BrukerService {

    suspend fun hentBruker(brukerid: String) : BrukerClass? = dbQuery {
        Bruker.select {
            (Bruker.brukernavn eq brukerid)
        }.mapNotNull {
            convertBruker(it)
        }.singleOrNull()
    }

    private val bcrypt = BCryptPasswordEncoder()
    fun kontrollerBruker(body: BrukerClass, dbbruker: BrukerClass): Boolean {
        return bcrypt.matches(body.passord, dbbruker.passord)
    }

    private fun convertBruker(row: ResultRow): BrukerClass {
        return BrukerClass(
                brukernavn = row[Bruker.brukernavn],
                passord = row[Bruker.passord]
        )
    }

}