package h577870.routes

import h577870.utils.brukerservice
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

@KtorExperimentalAPI
private fun Route.brukerRouting() {
    route("/bruker") {
        get("/{brukerid}") {
            val brukerid = call.parameters["brukerid"] ?: call.respondText("Bad request",
                    status = HttpStatusCode.BadRequest)
            val response = brukerservice.hentBruker(brukerid.toString()) ?: call.respondText("User does not exist",
                    status = HttpStatusCode.NotFound)
            call.respond(response)
        }
    }
}

@KtorExperimentalAPI
fun Application.registerBrukerRoutes() {
    routing {
        brukerRouting()
    }
}
