package com.fintecsystems.xs2awizard.networking.encryption

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import java.security.KeyPairGenerator
import java.security.Security

class EncryptorTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun registerBouncyCastle() {
            // "RSA/NONE/PKCS1Padding" is an Android/BouncyCastle-specific transformation not
            // present in the standard JVM Sun JCE provider. Register BouncyCastle so the
            // Encryptor can be instantiated in JVM unit tests.
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.insertProviderAt(BouncyCastleProvider(), 1)
            }
        }
    }

    /**
     * Generates a fresh RSA key pair for each test, constructs an [Encryptor] using the public key
     * modulus/exponent in hex, and verifies the output contract.
     */
    private fun createTestEncryptor(): Encryptor {
        val keyPair = KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
        }.generateKeyPair()

        val publicKey = keyPair.public as java.security.interfaces.RSAPublicKey
        val modulus = publicKey.modulus.toString(16)
        val exponent = publicKey.publicExponent.toString(16)

        return Encryptor(modulus, exponent)
    }

    @Test
    fun `encodeMessage output contains the double-colon separator`() {
        val encryptor = createTestEncryptor()
        val result = encryptor.encodeMessage("hello")
        assertTrue(
            "Output should contain '::' separator between encrypted password and message",
            result.contains("::")
        )
    }

    @Test
    fun `encodeMessage left part is a hex string`() {
        val encryptor = createTestEncryptor()
        val result = encryptor.encodeMessage("hello")
        val encryptedPassword = result.substringBefore("::")

        assertTrue("Encrypted password part should be non-empty", encryptedPassword.isNotEmpty())
        assertTrue(
            "Encrypted password part should consist only of hex characters",
            encryptedPassword.all { it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F' }
        )
    }

    @Test
    fun `encodeMessage right part is non-empty AES ciphertext`() {
        val encryptor = createTestEncryptor()
        val result = encryptor.encodeMessage("test message")
        val encryptedMessage = result.substringAfter("::")

        assertTrue("AES-encrypted message part should be non-empty", encryptedMessage.isNotEmpty())
    }

    @Test
    fun `encodeMessage produces different output on repeated calls due to random password`() {
        val encryptor = createTestEncryptor()
        val first = encryptor.encodeMessage("same input")
        val second = encryptor.encodeMessage("same input")

        assertNotEquals(
            "Two encryptions of the same plaintext should differ (random password per call)",
            first,
            second
        )
    }

    @Test
    fun `encodeMessage output has exactly one double-colon separator`() {
        val encryptor = createTestEncryptor()
        val result = encryptor.encodeMessage("hello world")
        assertEquals("Output should contain exactly one '::'", 1, result.split("::").size - 1)
    }

    @Test
    fun `encodeMessage works for empty string input`() {
        val encryptor = createTestEncryptor()
        val result = encryptor.encodeMessage("")
        assertTrue(result.contains("::"))
    }

    @Test
    fun `encodeMessage works for long input`() {
        val encryptor = createTestEncryptor()
        val longInput = "a".repeat(10_000)
        val result = encryptor.encodeMessage(longInput)
        assertTrue(result.contains("::"))
    }
}
