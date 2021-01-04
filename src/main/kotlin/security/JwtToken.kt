package h577870.security

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import h577870.entity.BrukerClass
import io.ktor.application.*
import java.util.*

class JwtToken(secret: String, audience: String) {

    private val validtime = 36_000_00 * 1
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .build()

    fun sign(bruker: BrukerClass, audience: String) : String {
        return JWT.create()
            .withClaim("brukernavn", bruker.brukernavn)
            .withClaim("passord", bruker.passord)
            .withAudience(audience)
            .withExpiresAt(Date(System.currentTimeMillis() + validtime))
            .sign(algorithm)
    }

}