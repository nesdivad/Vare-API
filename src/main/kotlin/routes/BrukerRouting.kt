package h577870.routes

import h577870.entity.BrukerClass
import h577870.utils.brukerservice
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

@KtorExperimentalAPI
private fun Route.brukerRouting() {
    route("/bruker") {
        get("{brukerid}") {
            val brukerid = call.parameters["brukerid"] ?: call.respondText("Bad request",
                    status = HttpStatusCode.BadRequest)
            val response = brukerservice.hentBruker(brukerid.toString()) ?: call.respondText("User does not exist",
                    status = HttpStatusCode.NotFound)
            call.respond(response)
        }
        /*
        For innlogging
         */
        post("logginn") {
            runCatching {
                val body = call.receive<BrukerClass>()
                val dbbruker = brukerservice.hentBruker(body.brukernavn)
                requireNotNull(dbbruker)
                if (BrukerClass.kontrollerBruker(body, dbbruker)) {
                    //Sende med token, sesjon osv.
                    call.respond(HttpStatusCode.Accepted, "Bruker ${body.brukernavn} er logget inn.")
                }
            }.onFailure {
                when (it) {
                    is ContentTransformationException -> call.respondText("Kunne ikke hente bruker fra request",
                        status = HttpStatusCode.BadRequest)
                    is IllegalArgumentException -> call.respondText("Bruker finnes ikke i databasen",
                        status = HttpStatusCode.NotFound)
                    else -> call.respondText("En feil oppsto: ${it.localizedMessage}")
                }
            }

        }
    }
}

@KtorExperimentalAPI
fun Application.registerBrukerRoutes() {
    routing {
        brukerRouting()
    }
}
