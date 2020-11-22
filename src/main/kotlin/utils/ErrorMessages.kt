package h577870.utils

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import java.lang.AssertionError

class ErrorMessages {

    companion object {
        suspend fun returnMessage(it: Throwable, call: ApplicationCall) {
            when (it) {
                is ContentTransformationException -> call.respondText("Kunne ikke hente body fra request",
                        status = HttpStatusCode.BadRequest)
                is AssertionError -> call.respondText("Finnes ikke i databasen",
                        status = HttpStatusCode.NotFound)
                is IllegalArgumentException -> call.respondText("Body fra request er feil",
                        status = HttpStatusCode.BadRequest)
                else -> call.respondText("En feil oppsto: ${it.localizedMessage}")
            }
        }
    }
}