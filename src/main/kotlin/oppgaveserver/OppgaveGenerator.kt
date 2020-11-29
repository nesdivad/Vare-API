package h577870.oppgaveserver

import h577870.entity.OppgaveClass
import h577870.entity.OppgaveStatus
import h577870.entity.OppgaveType
import h577870.entity.VareClass
import h577870.utils.oppgaveservice
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime

object OppgaveGenerator {
    init {

    }
    @ExperimentalSerializationApi
    fun generateBestilling(brukerid: String, kategori: String): OppgaveClass {
        return OppgaveClass(
            oppgaveid = 0,
            brukerid = brukerid,
            tittel = "${kategori}_BESTILLING_${LocalDateTime.now()}",
            beskrivelse = "Bestilling av ${kategori}varer.",
            vareliste = mutableMapOf(),
            type = OppgaveType.BESTILLING,
            status = OppgaveStatus.IKKESTARTET
        )
    }

    @KtorExperimentalAPI
    @ExperimentalSerializationApi
    suspend fun oppdaterBestilling(nyliste: Map<Long, Double>, status: OppgaveStatus, id: Int): String {
        val dboppgave = oppgaveservice.hentOppgaveMedId(id) ?: return "Finner ikke oppgave i database."
        nyliste.forEach { (t, u) ->
            dboppgave.vareliste.putIfAbsent(t,u) ?:
            dboppgave.vareliste[t]?.let { dboppgave.vareliste.replace(t, it, u) }
        }
        if (dboppgave.status == OppgaveStatus.IKKESTARTET) dboppgave.status = status
        oppgaveservice.oppdaterOppgave(dboppgave)
        return "Oppdatert oppgave med id ${dboppgave.oppgaveid}"
    }


}