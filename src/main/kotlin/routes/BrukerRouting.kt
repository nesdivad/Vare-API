package h577870.routes

import h577870.entity.BrukerClass
import h577870.module
import h577870.oppgaveserver.OppgaveGenerator
import h577870.security.JwtToken
import h577870.utils.ErrorMessages
import h577870.utils.brukerservice
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@KtorExperimentalAPI
private fun Route.brukerRouting() {
    val simplejwt = JwtToken(application.environment.config.property("jwt.secret").getString())
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
                if (brukerservice.kontrollerBruker(body, dbbruker)) {
                    call.respond(HttpStatusCode.Accepted, mapOf(
                        "jwttoken" to simplejwt.sign(dbbruker.brukernavn)
                    ))
                    OppgaveGenerator.initBrukerid(dbbruker.brukernavn)
                }
            }.onFailure {
                ErrorMessages.returnMessage(it, call)
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
