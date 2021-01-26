package h577870.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import kotlinx.datetime.*

/**
 * @author Kristoffer Davidsen
 * @param brukerid -> Oppgave tilhører bruker med id
 * @param tittel -> Tittel på oppgaven, genereres automatico.
 * @param beskrivelse -> Beskrivelse av oppgaven.
 * @param type -> Definerer hvilken type oppgave det er.
 * @param vareliste -> Inneholder liste over vareobjekter som det skal gjøres endringer på.
 * @param status -> Definerer oppgavens nåværende status.
 *
 */

/*
TODO: org.jetbrains.exposed.exceptions.ExposedSQLException: org.postgresql.util.PSQLException:
 ERROR: column oppgave.tidogdato does not exist
  Position: 126
 */

@ExperimentalSerializationApi
@Serializable
data class OppgaveClass(
        val oppgaveid: Int,
        val brukerid: String,
        val tittel: String,
        val beskrivelse: String,
        var vareliste: MutableMap<Long, Double>,
        @Serializable(with = OppgaveTypeSerializer::class)
        val type: OppgaveType,
        @Serializable(with = OppgaveStatusSerializer::class)
        var status: OppgaveStatus,
        @Serializable(with = TidSerializer::class)
        val tidogdato: Instant,
        @Serializable(with = TidSerializer::class)
        val tidsfrist: Instant
        )
/*
Vareliste i dette tilfelle vil være en Json-streng,
selv om det blir inkonsistent med lagringsform av varelisten i Kvittering i databasen.
Dette handler mest om utforsking av forskjellige måter å gjøre ting på :).
 */
@ExperimentalSerializationApi
object Oppgave : IntIdTable("oppgave", "id") {
        override val id: Column<EntityID<Int>>
                get() = super.id
        val brukerid: Column<String> = varchar("brukerid", length = 50)
                .references(Bruker.brukernavn)
        val tittel: Column<String> = varchar("tittel", length = 50)
        val beskrivelse: Column<String> = varchar("beskrivelse", length = 200)
        var vareliste: Column<String> = varchar("vareliste", length = 10485759)
        val type: Column<String> = varchar("type", length = 20)
        var status: Column<String> = varchar("status", length = 20)
        val tidogdato: Column<String> = varchar("tidogdato", length = 50)
        val tidsfrist: Column<String> = varchar("tidsfrist", length = 50)
}