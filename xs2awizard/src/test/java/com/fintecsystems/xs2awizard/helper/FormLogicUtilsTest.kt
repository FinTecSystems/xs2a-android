package com.fintecsystems.xs2awizard.helper

import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.PasswordLineData
import com.fintecsystems.xs2awizard.form.RedirectLineData
import com.fintecsystems.xs2awizard.form.SubmitLineData
import com.fintecsystems.xs2awizard.form.TextLineData
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for the pure business logic extracted from [com.fintecsystems.xs2awizard.components.XS2AWizardViewModel] into standalone
 * functions, enabling testing without an Android context or ViewModel lifecycle.
 */
class FormLogicUtilsTest {

    // ======================
    // buildFormJsonBody
    // ======================

    @Test
    fun `buildFormJsonBody always includes action field`() {
        val body = buildFormJsonBody(emptyList(), "submit")
        assertEquals("submit", body["action"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `buildFormJsonBody includes TextLineData value`() {
        val form = listOf(
            TextLineData(name = "username", value = JsonPrimitive("alice"))
        )
        val body = buildFormJsonBody(form, "submit")

        assertEquals("alice", body["username"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `buildFormJsonBody includes null value for TextLineData without value`() {
        val form = listOf(
            TextLineData(name = "username", value = null)
        )
        val body = buildFormJsonBody(form, "submit")

        assertEquals(JsonNull, body["username"])
    }

    @Test
    fun `buildFormJsonBody includes PasswordLineData value`() {
        val form = listOf(
            PasswordLineData(name = "pin", value = JsonPrimitive("1234"))
        )
        val body = buildFormJsonBody(form, "submit")

        assertEquals("1234", body["pin"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `buildFormJsonBody skips unchecked CheckBoxLineData`() {
        val form = listOf(
            CheckBoxLineData(
                name = "tos",
                value = JsonPrimitive(false),
                isLoginCredential = false,
            )
        )
        val body = buildFormJsonBody(form, "submit")

        assertNull(body["tos"])
    }

    @Test
    fun `buildFormJsonBody includes checked CheckBoxLineData`() {
        val form = listOf(
            CheckBoxLineData(
                name = "tos",
                value = JsonPrimitive(true),
                isLoginCredential = false,
            )
        )
        val body = buildFormJsonBody(form, "submit")

        assertEquals("true", body["tos"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `buildFormJsonBody skips non-ValueFormLineData elements`() {
        val form = listOf(
            SubmitLineData(label = "Submit"),
            TextLineData(name = "field", value = JsonPrimitive("value"))
        )
        val body = buildFormJsonBody(form, "submit")

        // SubmitLineData should not appear as a body field
        assertNull(body["label"])
        assertEquals("value", body["field"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `buildFormJsonBody merges values override`() {
        val form = listOf(TextLineData(name = "username", value = JsonPrimitive("original")))
        val override = JSONFormatter.formatter.parseToJsonElement("""{"tab":"online"}""")
            .let { it as kotlinx.serialization.json.JsonObject }

        val body = buildFormJsonBody(form, "switch-tab", override)

        assertEquals("switch-tab", body["action"]?.jsonPrimitive?.contentOrNull)
        assertEquals("online", body["tab"]?.jsonPrimitive?.contentOrNull)
        assertEquals("original", body["username"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `buildFormJsonBody with null form uses empty form`() {
        val body = buildFormJsonBody(null, "abort")

        assertEquals("abort", body["action"]?.jsonPrimitive?.contentOrNull)
        assertEquals(1, body.size) // only "action"
    }

    @Test
    fun `buildFormJsonBody with multiple fields includes all`() {
        val form = listOf(
            TextLineData(name = "username", value = JsonPrimitive("alice")),
            PasswordLineData(name = "password", value = JsonPrimitive("secret")),
            CheckBoxLineData(name = "remember", value = JsonPrimitive(true), isLoginCredential = false),
        )
        val body = buildFormJsonBody(form, "submit")

        assertEquals("alice", body["username"]?.jsonPrimitive?.contentOrNull)
        assertEquals("secret", body["password"]?.jsonPrimitive?.contentOrNull)
        assertEquals("true", body["remember"]?.jsonPrimitive?.contentOrNull)
        assertEquals("submit", body["action"]?.jsonPrimitive?.contentOrNull)
    }

    // ======================
    // filterFormLines
    // ======================

    @Test
    fun `filterFormLines returns null for null input`() {
        assertNull(filterFormLines(null))
    }

    @Test
    fun `filterFormLines keeps all elements when no duplicate errors`() {
        val form = listOf(
            TextLineData(name = "iban"),
            SubmitLineData(label = "Submit"),
        )
        val result = filterFormLines(form)
        assertEquals(2, result?.size)
    }

    @Test
    fun `filterFormLines removes error paragraph that duplicates a field validationError`() {
        val form = listOf(
            TextLineData(name = "iban", invalid = true, validationError = "Invalid IBAN"),
            ParagraphLineData(text = "Invalid IBAN", severity = "error"),
            SubmitLineData(label = "Submit"),
        )
        val result = filterFormLines(form)

        // The duplicate error paragraph should be removed
        assertEquals(2, result?.size)
        assertFalse(result?.any { it is ParagraphLineData } == true)
    }

    @Test
    fun `filterFormLines keeps non-error paragraphs even if text matches a validationError`() {
        val form = listOf(
            TextLineData(name = "iban", validationError = "Some text"),
            ParagraphLineData(text = "Some text", severity = "info"),
        )
        val result = filterFormLines(form)
        assertEquals(2, result?.size)
    }

    @Test
    fun `filterFormLines keeps error paragraph when no field has matching validationError`() {
        val form = listOf(
            TextLineData(name = "iban", validationError = "Different error"),
            ParagraphLineData(text = "Session expired", severity = "error"),
        )
        val result = filterFormLines(form)
        assertEquals(2, result?.size)
    }

    @Test
    fun `filterFormLines removes multiple duplicate error paragraphs`() {
        val form = listOf(
            TextLineData(name = "iban", invalid = true, validationError = "Error A"),
            TextLineData(name = "bic", invalid = true, validationError = "Error B"),
            ParagraphLineData(text = "Error A", severity = "error"),
            ParagraphLineData(text = "Error B", severity = "error"),
            SubmitLineData(label = "Submit"),
        )
        val result = filterFormLines(form)

        assertEquals(3, result?.size) // 2 text fields + submit
        assertFalse(result?.any { it is ParagraphLineData } == true)
    }

    @Test
    fun `filterFormLines keeps error paragraph when validationError is null`() {
        val form = listOf(
            TextLineData(name = "iban", validationError = null),
            ParagraphLineData(text = "Invalid IBAN", severity = "error"),
        )
        val result = filterFormLines(form)
        assertEquals(2, result?.size)
    }

    // ======================
    // isBackButtonPresent
    // ======================

    @Test
    fun `isBackButtonPresent returns false for null form`() {
        assertFalse(isBackButtonPresent(null))
    }

    @Test
    fun `isBackButtonPresent returns false for empty form`() {
        assertFalse(isBackButtonPresent(emptyList()))
    }

    @Test
    fun `isBackButtonPresent returns true when SubmitLineData has backLabel`() {
        val form = listOf(
            TextLineData(name = "field"),
            SubmitLineData(label = "Next", backLabel = "Go back"),
        )
        assertTrue(isBackButtonPresent(form))
    }

    @Test
    fun `isBackButtonPresent returns false when SubmitLineData has no backLabel`() {
        val form = listOf(SubmitLineData(label = "Submit", backLabel = null))
        assertFalse(isBackButtonPresent(form))
    }

    @Test
    fun `isBackButtonPresent returns false when SubmitLineData has empty backLabel`() {
        val form = listOf(SubmitLineData(label = "Submit", backLabel = ""))
        assertFalse(isBackButtonPresent(form))
    }

    @Test
    fun `isBackButtonPresent returns true when RedirectLineData has backLabel`() {
        val form = listOf(
            RedirectLineData(name = "bank_redirect", backLabel = "Cancel")
        )
        assertTrue(isBackButtonPresent(form))
    }

    @Test
    fun `isBackButtonPresent returns false when RedirectLineData has no backLabel`() {
        val form = listOf(RedirectLineData(name = "bank_redirect", backLabel = null))
        assertFalse(isBackButtonPresent(form))
    }

    @Test
    fun `isBackButtonPresent returns false for form with only text fields`() {
        val form = listOf(
            TextLineData(name = "username"),
            PasswordLineData(name = "password"),
        )
        assertFalse(isBackButtonPresent(form))
    }
}
