package com.quarkus.test.security.jwt

interface TokenGenerator {

    fun generate() : String

}
