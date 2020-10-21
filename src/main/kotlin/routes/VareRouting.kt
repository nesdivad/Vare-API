package h577870.routes

import h577870.dao.VareService
import h577870.utils.Validator
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import io.ktor.routing.*

//Objekt for databaseoperasjoner.
@KtorExperimentalAPI
val vareservice = VareService()
val validator = Validator()

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
        route("/{ean}") {
            /*
            Henter vare med ean-kode.
            Errorhandling ser litt rotete ut, wrapper det i en funksjon senere.
             */
            get {
                val ean = call.parameters["ean"] ?: call.respondText("Bad request",
                        status = HttpStatusCode.BadRequest
                )
                //Validering av ean
                runCatching { require(validator.validateEan(ean)) }
                        .onFailure { error -> print(error.message)
                                .also { call.respondText(
                        "Wrong format on ean", status = HttpStatusCode.BadRequest)
                                } }

                val vare = vareservice.hentVareMedEan(ean.toString().escapeHTML())
                        ?: call.respondText("Varen finnes ikke",
                        status = HttpStatusCode.NotFound
                )
                call.respond(vare)
            }
            //Query params liker den ikke...
            put("?pris={pris}") {
                val ean = call.parameters["ean"] ?:
                        call.respondText("Bad request for ean",
                        status = HttpStatusCode.BadRequest)
                val pris = call.parameters["pris"] ?:
                        call.respondText("Bad request for pris",
                        status = HttpStatusCode.BadRequest)
                //Errorhandling
                runCatching {
                    require(validator.validateEan(ean))
                    require(validator.validatePris(pris)) }
                        .onFailure { error -> print(error.message) }
                        .also { call.respondText("Wrong format for ean/pris",
                                status = HttpStatusCode.BadRequest) } //END runCatching
                //Returnerer enten ny pris eller Http.NotFound.
                val returnvalue = vareservice.oppdaterPris(ean.toString(), pris.toString()) ?:
                        call.respondText("Vare finnes ikke", status = HttpStatusCode.NotFound)
                call.respond(returnvalue)
            }
        } // END ean
    } //END vare
}
//Utvidelsesfunksjon for Application som henter endepunkter for vare.
@KtorExperimentalAPI
fun Application.registerVareRoutes() {
    routing {
        vareRoutes()
    }
}