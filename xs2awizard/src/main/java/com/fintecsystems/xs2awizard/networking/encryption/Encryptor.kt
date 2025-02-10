package com.fintecsystems.xs2awizard.networking.encryption

import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher
import com.github.mervick.aes_everywhere.legacy.Aes256
import java.lang.StringBuilder
import java.security.SecureRandom

/**
 * Helper class for encryption using RSA and AES
 */
internal class Encryptor(
    private val publicKey: PublicKey
) {
    private val cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")

    constructor(modulus: String, exponent: String) : this(constructKey(modulus, exponent))

    fun encodeMessage(message: String): String {
        // Generate Random password
        val password = generateRandomPassword(32)

        val encryptedMessage = Aes256.encrypt(message, password)

        // Encrypt password using the Public-Key and join bytes into hex string
        val encryptedPassword = cipher.let {
            it.init(Cipher.ENCRYPT_MODE, publicKey)
            it.doFinal(password.toByteArray())
        }.joinToString(separator = "", transform = { String.format("%02X", it) })

        return "$encryptedPassword::$encryptedMessage"
    }

    companion object {
        private val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        private fun generateRandomPassword(length: Int): String {
            val secureRandom = SecureRandom()

            return with(StringBuilder()) {
                repeat(length) {
                    append(allowedChars[secureRandom.nextInt(allowedChars.size)])
                }

                this.toString()
            }
        }

        private fun constructKey(modulus: String, exponent: String): PublicKey {
            val keySpec = RSAPublicKeySpec(BigInteger(modulus, 16), BigInteger(exponent, 16))
            return KeyFactory.getInstance("RSA").generatePublic(keySpec)
        }
    }
}
