package h577870

import h577870.dao.DatabaseFactory
import h577870.routes.registerBrukerRoutes
import h577870.routes.registerOppgaveRoutes
import h577870.routes.registerVareRoutes
import h577870.security.JwtToken
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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
}

