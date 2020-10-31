package h577870.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

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
        val brukerid: String,
        val tittel: String,
        val beskrivelse: String,
        val vareliste: List<VareClass>,
        @Serializable(with = OppgaveTypeSerializer::class)
        val type: Enum<OppgaveType>,
        @Serializable(with = OppgaveStatusSerializer::class)
        val status: Enum<OppgaveStatus>,

)