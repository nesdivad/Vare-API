package h577870.oppgaveserver

import h577870.entity.*
import h577870.utils.Oppgavehjelper
import h577870.utils.oppgaveservice
import h577870.utils.vareeservice
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime

@KtorExperimentalAPI
@ExperimentalSerializationApi
object OppgaveGenerator {

    private lateinit var brukerid: String

    /*
    Coroutine som overvåker oppgaver og vareegenskaper.
    Kaller callback bestillingsGenerator() dersom det må opereres på oppgaver.
     */
    fun runner() {
        var bool = true
        var egenskaper: MutableSet<VareEgenskaperClass>
        CoroutineScope(Dispatchers.IO).launch {
            while (bool) {
                egenskaper = vareeservice.hentAlle() as MutableSet<VareEgenskaperClass>
                /*
                Bestilling
                 */
                egenskaper.filter {
                    it.beholdning <= it.prestasjonslager
                }.also {
                    if (it.isNotEmpty()) bestillingsGenerator(it.toSet())
                }

                egenskaper.filter {
                    it.beholdning < 0
                }.also {
                    if (it.isNotEmpty()) kontrollGenerator(it.toSet())
                }

                delay(60000)
            }
        }
    }

    /*
    Lager en kontrolloppgave, f.eks. dersom en vare har beholdning i minus.
     */
    private fun kontrollGenerator(set: Set<VareEgenskaperClass>) {
        TODO()
    }

    /*
    Oppdaterer eller lager nye bestillingsoppgaver.
     */
    private suspend fun bestillingsGenerator(set: Set<VareEgenskaperClass>) {
        val existingOppgave = checkExistingBestilling()
        val convertedMap = Oppgavehjelper.setToMap(set)
        if (existingOppgave != null) {
            oppdaterBestilling(convertedMap, existingOppgave.status, existingOppgave.oppgaveid)
        }
        else {
            val id = generateBestilling(this.brukerid, convertedMap)
            print("Oppgave med id $id er lagt til i database.")
        }
    }

    /*
    Returnerer ny bestilling.
     */
    private suspend fun generateBestilling(brukerid: String, map: MutableMap<Long, Double>): Int {

        return oppgaveservice.leggTilOppgave(
        OppgaveClass(
            oppgaveid = 0,
            brukerid = brukerid,
            tittel = "BESTILLING_${LocalDateTime.now()}",
            beskrivelse = "Bestilling av varer.",
            vareliste = map,
            type = OppgaveType.BESTILLING,
            status = OppgaveStatus.IKKESTARTET,
            tidogdato = Clock.System.now(),
            tidsfrist = Oppgavehjelper.bestemFrist(OppgaveType.BESTILLING),
        ))
    }

    private suspend fun oppdaterBestilling(nyliste: Map<Long, Double>, status: OppgaveStatus, id: Int): String {
        val dboppgave = oppgaveservice.hentOppgaveMedId(id) ?: return "Finner ikke oppgave i database."
        nyliste.forEach { (t, u) ->
            dboppgave.vareliste.putIfAbsent(t,u) ?:
            dboppgave.vareliste[t]?.let { dboppgave.vareliste.replace(t, it, u) }
        }
        if (dboppgave.status == OppgaveStatus.IKKESTARTET) dboppgave.status = status
        oppgaveservice.oppdaterOppgave(dboppgave)
        return "Oppdatert oppgave med id ${dboppgave.oppgaveid}"
    }

    /**
     * @return true, hvis det finnes en eksisterende oppgave for dato, false ellers.
     */
    private suspend fun checkExistingBestilling(): OppgaveClass? {
        val frist = Oppgavehjelper.bestemFrist(OppgaveType.BESTILLING)
        return oppgaveservice.checkOppgave(frist, OppgaveType.BESTILLING)
    }

    fun initBrukerid(id: String) {
        brukerid = id
    }

    fun clearBrukerid() {
        brukerid = null.toString()
    }


}