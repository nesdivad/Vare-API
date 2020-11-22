package h577870.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

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

@ExperimentalSerializationApi
@Serializable
data class OppgaveClass(
        val oppgaveid: Int,
        val brukerid: String,
        val tittel: String,
        val beskrivelse: String,
        val vareliste: List<VareClass>,
        @Serializable(with = OppgaveTypeSerializer::class)
        val type: Enum<OppgaveType>,
        @Serializable(with = OppgaveStatusSerializer::class)
        val status: Enum<OppgaveStatus>,

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
}