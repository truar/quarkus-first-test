package com.quarkus.test.security.jwt

import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.Claims
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import java.security.PrivateKey
import java.time.Clock
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class JwtGenerator(
        @ConfigProperty(name = "private-key-file-name")
        val privateKeyFileName: String,
        @ConfigProperty(name = "token-duration-time")
        val tokenDuration: Int,
        val clock: Clock,
        val privateKeyReader: PrivateKeyReader) : TokenGenerator {

    override fun generate(): String {
        val privateKey = privateKeyReader.read(privateKeyFileName)
        return generateTokenString(privateKey, privateKeyFileName)
    }

    @Throws(Exception::class)
    fun generateTokenString(privateKey: PrivateKey, kid: String): String {
        val currentTimeInSecs = clock.instant().epochSecond
        val exp = currentTimeInSecs + tokenDuration

        val claims = JwtClaims()
        claims.issuer = "truaro"
        claims.audience = listOf("using-jwt-rbac")
        claims.subject = "jdoe-using-jwt-rbac"
        claims.setClaim(Claims.upn.name, "truaro")
        claims.issuedAt = NumericDate.fromSeconds(currentTimeInSecs)
        claims.setClaim(Claims.auth_time.name, NumericDate.fromSeconds(currentTimeInSecs))
        claims.expirationTime = NumericDate.fromSeconds(exp)
        val groups = Arrays.asList("user", "admin")
        claims.setClaim(Claims.groups.name, groups)

        val jws = JsonWebSignature()
        jws.payload = claims.toJson()
        jws.key = privateKey
        jws.keyIdHeaderValue = kid
        jws.setHeader("typ", "JWT")
        jws.algorithmHeaderValue = AlgorithmIdentifiers.RSA_USING_SHA256
        return jws.compactSerialization
    }
}