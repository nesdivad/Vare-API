package h577870.security

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtToken(secret: String) {

    private val validtime = 36_000_00 * 1
    private val algorithm = Algorithm.HMAC256(secret)
    private val issuer = "ktor.io"

    val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    fun sign(name: String) : String {
        return JWT.create().withClaim("name", name)
                .withExpiresAt(Date(System.currentTimeMillis() + validtime))
                .sign(algorithm)
    }

}