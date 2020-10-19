package h577870.routes

import h577870.dao.VareService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import io.ktor.routing.*

//Objekt for databaseoperasjoner.
@KtorExperimentalAPI
val vareservice = VareService()

@KtorExperimentalAPI
private fun Route.vareRoutes() {
    route("/vare") {
        //Alle varer
        get {
            val vareliste = vareservice.hentAlleVarer()
            if (vareliste.isNotEmpty()) call.respond(vareliste)
            else call.respondText("Liste med varer er tom / får ikke kontakt med database.",
            status = HttpStatusCode.NotFound)
        }
        //Henter vare med ean-kode.
        get("{ean}") {
            val ean = call.parameters["ean"] ?: call.respondText("Bad request",
                    status = HttpStatusCode.BadRequest
                )
            val vare = vareservice.hentVareMedEan(ean.toString()) ?: call.respondText("Varen finnes ikke",
                    status = HttpStatusCode.NotFound
                )
            call.respond(vare)
        }
    }
}
//Utvidelsesfunksjon for Application som henter endepunkter for vare.
@KtorExperimentalAPI
fun Application.registerVareRoutes() {
    routing {
        vareRoutes()
    }
}