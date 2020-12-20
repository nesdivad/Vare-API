package h577870

import h577870.controllers.ApplicationController
import h577870.dao.DatabaseFactory
import h577870.entity.*
import h577870.routes.registerBrukerRoutes
import h577870.routes.registerKasseRouting
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
import kotlinx.coroutines.CoroutineScope
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
    registerKasseRouting()
    //Initialiserer databasetilkobling.
    DatabaseFactory.init()
    ApplicationController.init()
}

