package h577870.routes

import h577870.entity.BrukerClass
import h577870.oppgaveserver.OppgaveGenerator
import h577870.security.JwtToken
import h577870.security.VareSession
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
        /*
        For innlogging
         */
        post("logginn") {
            runCatching {
                val body = call.receive<BrukerClass>()
                val dbbruker = brukerservice.hentBruker(body.brukernavn)
                requireNotNull(dbbruker)

                //Sjekk om det finnes sesjon fra før.
                if (call.sessions.get(dbbruker.brukernavn) != null) {
                    call.respond(HttpStatusCode.OK, "Already logged in.")
                }
                //Sjekk om brukernavn og/eller passord matcher.
                if (brukerservice.kontrollerBruker(body, dbbruker)) {
                    call.response.headers.append("jwttoken", simplejwt.sign(dbbruker.brukernavn))
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
