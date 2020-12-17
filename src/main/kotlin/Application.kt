package h577870

import h577870.dao.DatabaseFactory
import h577870.entity.*
import h577870.routes.registerBrukerRoutes
import h577870.routes.registerOppgaveRoutes
import h577870.routes.registerVareRoutes
import h577870.security.JwtToken
import h577870.utils.vareeservice
import h577870.utils.vareservice
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@ExperimentalSerializationApi
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val simpleJWT = JwtToken(environment.config.property("jwt.secret").getString())

    install(ContentNegotiation) {
        json()
    }

    /*
    install(Sessions) {
        header<SessionHeader>("SESSION")
    }
     */

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

    transaction {

    }

    /*
    val oppgave = OppgaveClass(
        oppgaveid = 10,
        brukerid = "admin",
        tittel = "Testoppgave",
        beskrivelse = "Foerste test av implementasjon",
        vareliste = mutableMapOf(
            7020655841165 to 2.0,
            7032069723586 to 4.0,
            5021991941757 to 10.0,
            7032069730249 to 7.0,
            7038080080882 to 6.0
        ),
        type = OppgaveType.KONTROLL,
        status = OppgaveStatus.PAAGAAR
    )
    print(Json.encodeToString(oppgave))

     */
}

