package com.quarkus.test.security.web

import com.quarkus.test.security.jwt.TokenGenerator
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/login")
class AuthenticationResource(val tokenGenerator: TokenGenerator) {

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    fun login(userCredentials: UserCredentials): String {
        return tokenGenerator.generate()
    }

    @GET
    @Path("/secured")
    @RolesAllowed("user")
    fun helloSecured() = "secured"
}
