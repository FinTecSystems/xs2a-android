package com.fintecsystems.xs2awizard.form.serializers

import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.FormLineData
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests the custom [CheckBoxLineDataSerializer] which has special logic for `isLoginCredential`
 * (hardcoded to true when name == "privacy_policy") and maps the JSON `checked` field to `value`.
 */
class CheckBoxLineDataSerializerTest {

    private val json = JSONFormatter.formatter

    // --- deserialise ---

    @Test
    fun `deserialise checked true sets value to JsonPrimitive true`() {
        val raw = """{"type":"checkbox","name":"accept","checked":true}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertTrue(data.value?.jsonPrimitive?.boolean == true)
    }

    @Test
    fun `deserialise checked false sets value to JsonPrimitive false`() {
        val raw = """{"type":"checkbox","name":"accept","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertFalse(data.value?.jsonPrimitive?.boolean == true)
    }

    @Test
    fun `deserialise missing checked defaults to false`() {
        val raw = """{"type":"checkbox","name":"accept"}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertFalse(data.value?.jsonPrimitive?.booleanOrNull == true)
    }

    @Test
    fun `deserialise privacy_policy name sets isLoginCredential to true`() {
        val raw = """{"type":"checkbox","name":"privacy_policy","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertTrue(data.isLoginCredential == true)
    }

    @Test
    fun `deserialise non-privacy_policy name sets isLoginCredential to false`() {
        val raw = """{"type":"checkbox","name":"consent","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertFalse(data.isLoginCredential == true)
    }

    @Test
    fun `deserialise label is preserved`() {
        val raw = """{"type":"checkbox","name":"tos","label":"I accept the Terms of Service","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertEquals("I accept the Terms of Service", data.label)
    }

    @Test
    fun `deserialise null label`() {
        val raw = """{"type":"checkbox","name":"accept","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertNull(data.label)
    }

    @Test
    fun `deserialise disabled flag`() {
        val raw = """{"type":"checkbox","name":"tos","checked":false,"disabled":true}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertTrue(data.disabled == true)
    }

    @Test
    fun `deserialise invalid and required flags`() {
        val raw = """{"type":"checkbox","name":"tos","checked":false,"invalid":true,"required":true}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertTrue(data.invalid)
        assertTrue(data.required)
    }

    @Test
    fun `deserialise validation_error`() {
        val raw = """{"type":"checkbox","name":"tos","checked":false,"validation_error":"Must be checked"}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertEquals("Must be checked", data.validationError)
    }

    // --- serialise round-trip ---

    @Test
    fun `serialise and re-deserialise preserves checked state`() {
        val original = CheckBoxLineData(
            name = "terms",
            label = "Accept",
            isLoginCredential = false,
            value = JsonPrimitive(true),
            validationError = null,
            validation = null,
            invalid = false,
            required = false,
            disabled = false,
        )

        val serialised = json.encodeToString(CheckBoxLineData.serializer(), original)
        val restored = json.decodeFromString<CheckBoxLineData>(serialised)

        assertEquals("terms", restored.name)
        assertTrue(restored.value?.jsonPrimitive?.boolean == true)
    }

    @Test
    fun `serialise and re-deserialise unchecked box`() {
        val original = CheckBoxLineData(
            name = "accept",
            label = null,
            isLoginCredential = false,
            value = JsonPrimitive(false),
            validationError = null,
            validation = null,
            invalid = false,
            required = false,
            disabled = false,
        )

        val serialised = json.encodeToString(CheckBoxLineData.serializer(), original)
        val restored = json.decodeFromString<CheckBoxLineData>(serialised)

        assertFalse(restored.value?.jsonPrimitive?.boolean == true)
    }
}
