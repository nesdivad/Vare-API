package h577870.routes

import h577870.entity.OppgaveClass
import h577870.utils.oppgaveservice
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@KtorExperimentalAPI
private fun Route.oppgaveRoutes() {
    route("oppgave") {
        /*
        Hent alle oppgaver for en bruker
         */
        get("{brukerid}") {
            val brukerid = call.parameters["brukerid"] ?: call.respondText(
                "Bad request for parameter {brukerid}",
                status = HttpStatusCode.BadRequest
            )
            val response = oppgaveservice.hentOppgaverMedBrukerid(brukerid.toString())
                ?: call.respondText("Brukerid finnes ikke", status = HttpStatusCode.NotFound)
            call.respond(response)
        }
        post("ny") {
            runCatching {
                val body = call.receive<OppgaveClass>()
                val id = oppgaveservice.leggTilOppgave(body)
                call.respondText("Lagt til oppgave med id ${id.value}", status = HttpStatusCode.OK)
            }.onFailure {
                //Lage mer detaljerte feilmeldinger, som i brukerrouting.
                call.respondText("En feil oppsto: ${it.localizedMessage}")
            }
        }
        put("oppdater") {
            runCatching {
                val body = call.receive<OppgaveClass>()
                requireNotNull(body.oppgaveid)
                val id = oppgaveservice.oppdaterOppgave(body)
                call.respondText("Oppdatert oppgave med id ${id.value}", status = HttpStatusCode.OK)
            }.onFailure {
                call.respondText("En feil oppsto: ${it.localizedMessage}")
            }
        }
    }
}

@KtorExperimentalAPI
@ExperimentalSerializationApi
fun Application.registerOppgaveRoutes() {
    routing {
        oppgaveRoutes()
    }
}