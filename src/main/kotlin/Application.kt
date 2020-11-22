package h577870

import h577870.dao.DatabaseFactory
import h577870.entity.OppgaveClass
import h577870.entity.OppgaveStatus
import h577870.entity.OppgaveType
import h577870.entity.VareClass
import h577870.routes.registerBrukerRoutes
import h577870.routes.registerOppgaveRoutes
import h577870.routes.registerVareRoutes
import h577870.security.JwtToken
import h577870.utils.oppgaveservice
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.util.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@ExperimentalSerializationApi
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    /*
    Implementeres senere...
     */
    val simpleJWT = JwtToken(environment.config.property("jwt.secret").getString())

    /*
    Content-type til json.
     */
    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        jwt {
            verifier(simpleJWT.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }
    }

    //Registrerer endepunkter for Vare-databasen.
    registerVareRoutes()
    registerBrukerRoutes()
    registerOppgaveRoutes()
    //Initialiserer databasetilkobling.
    DatabaseFactory.init()

    /*
    val oppgave = OppgaveClass(
            oppgaveid = 10,
            brukerid = "admin",
            tittel = "Testoppgave",
            beskrivelse = "Foerste test av implementasjon",
            vareliste = listOf(
                    VareClass(7020655841165, "Lettkokte Havregryn Urkraft", 25,
                            "Norgesmøllene AS", sortimentskode = "A1", kategori = "Tørr", plu = null),
                    VareClass(7032069723586, "Økologisk Olivenolje", 85, "Kolonihagen",
                            sortimentskode = "A2", kategori = "Tørr", plu = null),
                    VareClass(5021991941757, "Kamillete Snore & Peace", 40,
                            "Clipper Teas AS", sortimentskode = "A2", kategori = "Tørr", plu = null),
                    VareClass(7032069730249, "Pizzamel Tipo 00", 35, "Kolonihagen",
                            sortimentskode = "A2", kategori = "Tørr", plu = null),
                    VareClass(7038080080882, "Mors Pizzagjær", 20, "Idun Industri AS",
                            sortimentskode = "A1", kategori = "Tørr", plu = null)
            ),
            type = OppgaveType.KONTROLL,
            status = OppgaveStatus.PAAGAAR
    )
    print(Json.encodeToString(oppgave))

     */
}

