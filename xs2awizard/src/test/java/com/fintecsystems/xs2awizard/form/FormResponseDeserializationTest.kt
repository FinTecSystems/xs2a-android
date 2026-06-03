package com.fintecsystems.xs2awizard.form

import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests full [FormResponse] deserialisation from realistic JSON payloads, verifying the
 * end-to-end contract between raw backend responses and the typed model.
 */
class FormResponseDeserializationTest {

    private val json = JSONFormatter.formatter

    @Test
    fun `deserialise empty form response`() {
        val raw = """{}"""
        val response = json.decodeFromString<FormResponse>(raw)

        assertNull(response.form)
        assertNull(response.language)
        assertNull(response.callback)
        assertNull(response.error)
    }

    @Test
    fun `deserialise login form with text and password fields`() {
        val raw = """
            {
              "language": "de",
              "form": [
                {"type":"text","name":"username","label":"Username","login_credential":true},
                {"type":"password","name":"password","label":"Password","login_credential":true},
                {"type":"submit","label":"Login","back":"Back"}
              ]
            }
        """.trimIndent()

        val response = json.decodeFromString<FormResponse>(raw)

        assertEquals(XS2AWizardLanguage.DE, response.language)
        assertNotNull(response.form)
        val form = response.form!!
        assertEquals(3, form.size)

        val username = form[0] as TextLineData
        assertEquals("username", username.name)
        assertTrue(username.isLoginCredential == true)

        val password = form[1] as PasswordLineData
        assertEquals("password", password.name)

        val submit = form[2] as SubmitLineData
        assertEquals("Login", submit.label)
        assertEquals("Back", submit.backLabel)
    }

    @Test
    fun `deserialise finish callback with transaction credentials`() {
        val raw = """
            {
              "callback": "finish",
              "callbackParams": ["txn_abc123"]
            }
        """.trimIndent()

        val response = json.decodeFromString<FormResponse>(raw)

        assertEquals("finish", response.callback)
        assertEquals("txn_abc123", response.callbackParams?.get(0)?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `deserialise abort callback`() {
        val raw = """{"callback":"abort"}"""
        val response = json.decodeFromString<FormResponse>(raw)
        assertEquals("abort", response.callback)
    }

    @Test
    fun `deserialise error response`() {
        val raw = """{"error":"login_failed","isErrorRecoverable":true}"""
        val response = json.decodeFromString<FormResponse>(raw)

        assertEquals("login_failed", response.error)
        assertTrue(response.isErrorRecoverable == true)
    }

    @Test
    fun `deserialise form with validation error on field`() {
        val raw = """
            {
              "form": [
                {
                  "type": "text",
                  "name": "iban",
                  "label": "IBAN",
                  "invalid": true,
                  "validation_error": "Invalid IBAN format"
                },
                {
                  "type": "paragraph",
                  "text": "Invalid IBAN format",
                  "severity": "error"
                },
                {"type": "submit", "label": "Submit"}
              ]
            }
        """.trimIndent()

        val response = json.decodeFromString<FormResponse>(raw)
        val form = response.form!!

        assertEquals(3, form.size)

        val ibanField = form[0] as TextLineData
        assertTrue(ibanField.invalid)
        assertEquals("Invalid IBAN format", ibanField.validationError)

        val errorParagraph = form[1] as ParagraphLineData
        assertEquals("Invalid IBAN format", errorParagraph.text)
        assertEquals("error", errorParagraph.severity)
    }

    @Test
    fun `deserialise mixed form with tabs, logo and description`() {
        val raw = """
            {
              "form": [
                {"type":"logo"},
                {"type":"description","text":"Please select your bank."},
                {"type":"tabs","action":"select","selected":"tab_online","tabs":{"tab_online":"Online Banking"}},
                {"type":"text","name":"search","label":"Search bank"},
                {"type":"submit","label":"Next"}
              ]
            }
        """.trimIndent()

        val response = json.decodeFromString<FormResponse>(raw)
        val form = response.form!!

        assertEquals(5, form.size)
        assertTrue(form[0] is LogoLineData)
        assertTrue(form[1] is DescriptionLineData)
        assertTrue(form[2] is TabsLineData)
        assertTrue(form[3] is TextLineData)
        assertTrue(form[4] is SubmitLineData)
    }

    @Test
    fun `deserialise response ignores unknown top-level fields`() {
        val raw = """{"unknown_field":"value","form":[],"future_field":42}"""
        val response = json.decodeFromString<FormResponse>(raw)
        assertNotNull(response.form)
        assertEquals(0, response.form!!.size)
    }

    @Test
    fun `deserialise step field`() {
        val raw = """{"step":"login","form":[]}"""
        val response = json.decodeFromString<FormResponse>(raw)
        assertEquals("login", response.step)
    }
}
