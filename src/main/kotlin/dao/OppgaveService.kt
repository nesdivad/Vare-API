package h577870.dao

import h577870.dao.DatabaseFactory.dbQuery
import h577870.entity.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

@KtorExperimentalAPI
@ExperimentalSerializationApi
class OppgaveService {

    suspend fun hentOppgaveMedId(oppgaveid: Int): OppgaveClass? = dbQuery {
        Oppgave.select {
            (Oppgave.oppgaveid eq oppgaveid)
        }.mapNotNull {
            convertJsonToClass(it)
        }.singleOrNull()
    }

    suspend fun leggTilOppgave(oppgaveClass: OppgaveClass): EntityID<Int> = dbQuery {
        return@dbQuery Oppgave.insertAndGetId {
            it[brukerid] = oppgaveClass.brukerid
            it[tittel] = oppgaveClass.tittel
            it[beskrivelse] = oppgaveClass.beskrivelse
            it[vareliste] = Json.encodeToString(oppgaveClass.vareliste)
            it[type] = Json.encodeToString(OppgaveTypeSerializer, oppgaveClass.type as OppgaveType)
            it[status] = Json.encodeToString(OppgaveStatusSerializer, oppgaveClass.status as OppgaveStatus)
        }
    }

    suspend fun hentOppgaverMedBrukerid(brukerid: String): List<OppgaveClass>? = dbQuery {
        Oppgave.selectAll().andWhere {
            (Oppgave.brukerid eq brukerid)
        }.mapNotNull {
            convertJsonToClass(it)
        }.toList()
    }

    suspend fun oppdaterOppgave(oppgaveClass: OppgaveClass): EntityID<Int> = dbQuery {
        return@dbQuery Oppgave.insertAndGetId {

        }
    }

    @ExperimentalSerializationApi
    private fun convertJsonToClass(row: ResultRow) : OppgaveClass {

        val listFromRow = row[Oppgave.vareliste]

        return OppgaveClass(
                oppgaveid = row[Oppgave.oppgaveid],
                brukerid = row[Oppgave.brukerid],
                tittel = row[Oppgave.tittel],
                beskrivelse = row[Oppgave.beskrivelse],
                type = Json.decodeFromString(OppgaveTypeSerializer, string = row[Oppgave.type]),
                status = Json.decodeFromString(OppgaveStatusSerializer, string = row[Oppgave.status]),
                vareliste = Json.decodeFromString(listFromRow)
        )
    }
}
