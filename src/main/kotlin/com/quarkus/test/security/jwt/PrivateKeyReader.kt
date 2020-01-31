package com.quarkus.test.security.jwt

import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PrivateKeyReader {

    fun read(privateKeyFileName: String): PrivateKey {
        return readPrivateKey(privateKeyFileName)
    }

    /**
     * Read a PEM encoded private key from the classpath
     *
     * @param pemResName - key file resource name
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    @Throws(Exception::class)
    fun readPrivateKey(pemResName: String): PrivateKey {
        val contentIS = this.javaClass.getResourceAsStream(pemResName)
        val tmp = ByteArray(4096)
        val length = contentIS.read(tmp)
        return decodePrivateKey(String(tmp, 0, length, Charset.forName("UTF-8")))
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     *
     * @param pemEncoded - PEM string for private key
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    @Throws(Exception::class)
    fun decodePrivateKey(pemEncoded: String): PrivateKey {
        val encodedBytes = toEncodedBytes(pemEncoded)
        val keySpec = PKCS8EncodedKeySpec(encodedBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(keySpec)
    }

    private fun toEncodedBytes(pemEncoded: String): ByteArray {
        val normalizedPem = removeBeginEnd(pemEncoded)
        return Base64.getDecoder().decode(normalizedPem)
    }

    private fun removeBeginEnd(pem: String): String {
        var pem = pem
        pem = pem.replace("-----BEGIN (.*)-----".toRegex(), "")
        pem = pem.replace("-----END (.*)----".toRegex(), "")
        pem = pem.replace("\r\n".toRegex(), "")
        pem = pem.replace("\n".toRegex(), "")
        return pem.trim { it <= ' ' }
    }
}
