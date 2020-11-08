package h577870.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KClass

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
object Oppgave : Table() {
        val oppgaveid: Column<Int> = integer("oppgaveid").autoIncrement("Oppgave_oppgaveid_seq")
        val brukerid: Column<String> = varchar("brukerid", length = 50)
                .references(Bruker.brukernavn)
        val tittel: Column<String> = varchar("tittel", length = 50)
        val beskrivelse: Column<String> = varchar("beskrivelse", length = 200)
        var vareliste: Column<String> = varchar("vareliste", length = Int.MAX_VALUE)
        val type: Column<OppgaveType> = enumeration("type", OppgaveType::class)
        var status: Column<OppgaveStatus> = enumeration("status", OppgaveStatus::class)
        val oppgave_key = PrimaryKey(oppgaveid, name = "oppgave_pkey")
}