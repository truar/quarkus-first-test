package com.quarkus.test.security.web

import com.quarkus.test.security.jwt.TokenGenerator
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.ws.rs.core.MediaType

@QuarkusTest
class AuthenticationResourceTest {

    @Inject
    lateinit var tokenGenerator: TokenGenerator

    @Test
    fun userCanLoginByProvidingUsernameAndPassword() {
        val s = "{\"username\":\"toto\",\"password\":\"toto\"}"
        Given {
            body(s)
            contentType(MediaType.APPLICATION_JSON)
        } When {
            post("/login")
        } Then {
            statusCode(200)
            contentType(MediaType.APPLICATION_JSON)
            body(notNullValue())
        }
    }

    @Test
    fun userWithTokenCanAccessSecuredResources() {
        val token = tokenGenerator.generate()
        Given {
            auth().oauth2(token)
        } When {
            get("/login/secured")
        } Then {
            statusCode(200)
            body(equalTo("secured"))
        }
    }
}
