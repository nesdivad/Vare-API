package h577870.routes

import h577870.dao.VareService
import h577870.entity.VareClass
import h577870.utils.ErrorMessages
import h577870.utils.Validator
import h577870.utils.validator
import h577870.utils.vareservice
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

//Objekt for databaseoperasjoner.
@KtorExperimentalAPI

private fun Route.vareRoutesGet() {

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
                        status = HttpStatusCode.BadRequest)
                val escaped = ean.toString().escapeHTML()
                //Validering av ean
                runCatching { require(validator.validateEan(escaped)) }
                        .onFailure { ErrorMessages.returnMessage(it, call) }

                val vare = vareservice.hentVareMedEan(escaped)
                        ?: call.respondText("Varen finnes ikke",
                                status = HttpStatusCode.NotFound
                        )
                call.respond(vare)
            }
        } // END ean
    } //END vare
}

/*
TODO:Authentication
 */

@KtorExperimentalAPI
private fun Route.vareRoutesPost() {
        route("/vare") {
            /*
            TODO: Escape JSON-obj.
             */
            put("updatePris") {
                runCatching {
                    val body = call.receive<VareClass>()
                    when (vareservice.oppdaterVare(body) ?: 0) {
                        0 -> call.respondText("Error updating...", status = HttpStatusCode.NotFound)
                        else -> call.respondText("Updated price on vare with ean ${body.ean} to ${body.pris}")
                    }
                }.onFailure { ErrorMessages.returnMessage(it, call) }
            }//end PUT

            post("nyVare") {
                runCatching {
                    val body = call.receive<VareClass>()
                    vareservice.leggTilVare(body)
                    call.respondText("Successfully added vare with ean ${body.ean}",
                            status = HttpStatusCode.OK)
                }.onFailure { ErrorMessages.returnMessage(it, call) }
            }
        }
    /*
    For testing purposes
     */
        route("/test") {
            
        }
    }

//Utvidelsesfunksjon for Application som henter endepunkter for vare.
@KtorExperimentalAPI
fun Application.registerVareRoutes() {
    routing {
        vareRoutesGet()
        vareRoutesPost()
    }
}
