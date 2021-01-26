package h577870.routes

import h577870.entity.OppgaveClass
import h577870.utils.ErrorMessages
import h577870.utils.oppgaveservice
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi

/*
TODO: Sjekke sesjon for alle ruter.
 */

@ExperimentalSerializationApi
@KtorExperimentalAPI
private fun Route.oppgaveRoutes() {
    authenticate {
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
                call.respond(HttpStatusCode.OK, response)
            }
            post("ny") {
                runCatching {
                    val body = call.receive<OppgaveClass>()
                    val id = oppgaveservice.leggTilOppgave(body)
                    call.respondText("Lagt til oppgave med id $id", status = HttpStatusCode.OK)
                }.onFailure {
                    ErrorMessages.returnMessage(it, call)
                }
            }
            put("oppdater") {
                runCatching {
                    val body = call.receive<OppgaveClass>()
                    requireNotNull(body.oppgaveid)
                    val id = oppgaveservice.oppdaterOppgave(body)
                    assert(body.oppgaveid == id)
                    call.respondText("Oppdatert oppgave med id $id", status = HttpStatusCode.OK)
                }.onFailure {
                    ErrorMessages.returnMessage(it, call)
                }
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