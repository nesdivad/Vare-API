package h577870.routes

import h577870.entity.BrukerClass
import h577870.oppgaveserver.OppgaveGenerator
import h577870.security.JwtToken
import h577870.security.VareSession
import h577870.utils.ErrorMessages
import h577870.utils.brukerservice
import io.ktor.application.*
import io.ktor.auth.*
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

    val audience = application.environment.config.property("jwt.audience").getString()
    val simplejwt = JwtToken(application.environment.config.property("jwt.secret").getString(),
        audience)

    route("/bruker") {
        /*
        For innlogging
         */
        post("logginn") {
            runCatching {
                val body = call.receive<BrukerClass>()
                val dbbruker = brukerservice.hentBruker(body.brukernavn)
                requireNotNull(dbbruker)

                //Sjekk om brukernavn og/eller passord matcher.
                if (brukerservice.kontrollerBruker(body, dbbruker)) {
                    //Sjekk om det finnes sesjon fra før.
                    val token = simplejwt.sign(dbbruker, audience)
                    call.response.headers.append(name = "jwt", value = token)
                    //Lager ny serversesjon.
                    call.sessions.set(VareSession(dbbruker.brukernavn, 300))
                    call.respond(HttpStatusCode.OK, "Logged in.")
                    //Forteller oppgavegenerator hvilken bruker som er logget inn.
                    OppgaveGenerator.initBrukerid(dbbruker.brukernavn)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are not correct.")
                }
            }.onFailure {
                ErrorMessages.returnMessage(it, call)
            }

        }
        post("/loggut") {
            val body = call.receive<String>() //bruker
            val usertoken = call.request.authorization() ?: ""
            if (call.sessions.get(body) != null && usertoken.isNotEmpty()) {
                call.sessions.clear(body)
                call.respondText(status = HttpStatusCode.OK, text = "Logged out.")
            }
            else {
                call.respondText(text = "Already logged out", status = HttpStatusCode.OK)
            }
            OppgaveGenerator.clearBrukerid()
        }
    }
}

@ExperimentalSerializationApi
@KtorExperimentalAPI
fun Application.registerBrukerRoutes() {
    routing {
        brukerRouting()
    }
}
