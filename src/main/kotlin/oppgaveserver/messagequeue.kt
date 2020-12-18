package h577870.oppgaveserver

import h577870.entity.OppgaveClass
import h577870.entity.VareClass
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.NoSuchElementException

@ExperimentalSerializationApi
/*
Singleton for messagequeue
 */
object Oppgaveequeue {

    private val oppgaveliste: LinkedList<OppgaveClass> = LinkedList()
    /*         Internal methods             */

    @Synchronized fun addElement(oppgave: OppgaveClass) {
        oppgaveliste.add(oppgave)
    }

    @Synchronized fun addBatch(oppgavebatch: List<OppgaveClass>) {
        oppgavebatch.forEach { elem -> oppgaveliste.add(elem) }
    }

    @Synchronized fun clearAll() {
       try { oppgaveliste.clear() }
       catch (e: UnsupportedOperationException) { print(e.stackTrace) }
    }
    /*----------------------------------------------------*/
    /*    API methods    */
    @Synchronized fun retrieveAllJson() : String {
        return try {
            Json.encodeToString(oppgaveliste.asSequence())
        } catch (e: NoSuchElementException) {
            "204"
        }
    }

    @Synchronized fun retrieveAndClearOne() : String {
        return try {
            Json.encodeToString(oppgaveliste.remove())
        } catch (e: NoSuchElementException) {
            "204" //Http Code for No Content
        }
    }
}

object Kassequeue {

    private val kasseliste: MutableList<MutableMap<VareClass, Double>> = mutableListOf()

    @Synchronized fun addMelding(melding: String) {
        val decoded = Json.decodeFromString<MutableMap<VareClass, Double>>(melding)
        kasseliste.add(decoded)
    }

    @Synchronized fun retrieveMelding() : MutableMap<VareClass, Double> {
        return kasseliste.removeFirst()
    }

}