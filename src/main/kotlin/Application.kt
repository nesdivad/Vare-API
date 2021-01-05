package h577870

import h577870.controllers.ApplicationController
import h577870.dao.DatabaseFactory
import h577870.entity.BrukerClass
import h577870.routes.registerBrukerRoutes
import h577870.routes.registerKasseRouting
import h577870.routes.registerOppgaveRoutes
import h577870.routes.registerVareRoutes
import h577870.security.JwtToken
import h577870.security.VareSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@ExperimentalTime
@ExperimentalSerializationApi
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val simpleJWT = JwtToken(environment.config.property("jwt.secret").getString(),
        environment.config.property("jwt.audience").getString())
    val jwtrealm = environment.config.property("jwt.realm").getString()

    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        maxAgeDuration = Duration.INFINITE
    }

    install(Sessions) {
        val secret = hex(environment.config.property("session.secret").getString())
        cookie<VareSession>("SERVER_SESSION", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "Lax"
            cookie.httpOnly = true
            cookie.maxAgeInSeconds = 300L
            transform(SessionTransportTransformerMessageAuthentication(secret, "HmacSHA256"))
        }
    }

    install(Authentication) {
        jwt {
            realm = jwtrealm
            verifier(simpleJWT.verifier)
            validate {
                val username = it.payload.getClaim("brukernavn").asString()
                val password = it.payload.getClaim("passord").asString()
                if (username != null && password != null) {
                    BrukerClass(username, password)
                } else null
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

