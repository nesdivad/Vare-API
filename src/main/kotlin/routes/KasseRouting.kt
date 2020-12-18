package h577870.routes

import h577870.oppgaveserver.Kassequeue
import h577870.utils.ErrorMessages
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.kasseRouting() {
    route("kasse") {
        post("salg") {
            runCatching {
                val body = call.receive<String>()
                Kassequeue.addMelding(body)
                call.respondText(text = "Accepted", status = HttpStatusCode.Accepted)
            }.onFailure {
                ErrorMessages.returnMessage(it, call)
            }
        }
    }
}

fun Application.registerKasseRouting() {
    routing {
        kasseRouting()
    }
}