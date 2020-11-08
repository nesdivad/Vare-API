package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.Oppgave
import h577870.entity.OppgaveClass
import h577870.entity.VareClass
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

@KtorExperimentalAPI
class OppgaveService {

    @ExperimentalSerializationApi
    suspend fun hentOppgaveMedId(oppgaveid: Int): OppgaveClass? = dbQuery {
        Oppgave.select {
            (Oppgave.oppgaveid eq oppgaveid)
        }.mapNotNull {
            convertJsonToClass(it)
        }.singleOrNull()
    }

    @ExperimentalSerializationApi
    private fun convertJsonToClass(row: ResultRow) : OppgaveClass {

        val listFromRow = row[Oppgave.vareliste]
        val objectlist = Json.decodeFromString<List<VareClass>>(listFromRow)

        return OppgaveClass(
                oppgaveid = row[Oppgave.oppgaveid],
                brukerid = row[Oppgave.brukerid],
                tittel = row[Oppgave.tittel],
                beskrivelse = row[Oppgave.beskrivelse],
                type = row[Oppgave.type],
                status = row[Oppgave.status],
                vareliste = objectlist
        )
    }
}
