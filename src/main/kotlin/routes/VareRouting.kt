package h577870.routes

import h577870.dao.VareService
import h577870.entity.VareClass
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
                        .onFailure { error -> print(error.message)
                                    .also {
                                        call.respondText("Wrong format on ean",
                                                status = HttpStatusCode.BadRequest)
                                    }
                        }

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
                val body = call.receive<VareClass>()
                val nyVare = VareClass(
                        ean = body.ean,
                        navn = body.navn,
                        pris = body.pris,
                        beskrivelse = body.beskrivelse,
                        sortimentskode = body.sortimentskode,
                        plu = body.plu,
                        kategori = body.kategori)
                runCatching {
                    when (vareservice.oppdaterVare(nyVare) ?: 0) {
                        0 -> call.respondText("Error updating...", status = HttpStatusCode.NotFound)
                        else -> call.respondText("Updated price on vare with ean ${nyVare.ean} to ${nyVare.pris}")
                    }
                }.onFailure { error -> print(error) }
            }//end PUT

            post("addVare") {
                val body = call.receive<VareClass>()
                val nyVare = VareClass(
                        ean = body.ean,
                        navn = body.navn,
                        pris = body.pris,
                        beskrivelse = body.beskrivelse,
                        sortimentskode = body.sortimentskode,
                        plu = body.plu,
                        kategori = body.kategori)
                runCatching {
                    vareservice.leggTilVare(nyVare)
                    call.respondText("Successfully added vare with ean ${nyVare.ean}",
                            status = HttpStatusCode.OK)
                }.onFailure { error -> print(error) }
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
