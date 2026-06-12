package com.fintecsystems.xs2awizard.form

import com.fintecsystems.xs2awizard.helper.JSONFormatter
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies that each [FormLineData] subtype deserialises correctly from JSON, ensuring that the
 * JSON protocol contract with the XS2A backend does not silently break during refactoring.
 */
class FormLineDataSerializationTest {

    private val json = JSONFormatter.formatter

    // --- TextLineData ---

    @Test
    fun `deserialise TextLineData with all fields`() {
        val raw = """{"type":"text","name":"username","label":"Username","value":"alice",
            |"validation":"required","validation_error":null,"invalid":false,
            |"login_credential":true,"disabled":false,"placeholder":"Enter name",
            |"autocomplete_action":"bank","override_type":"email","maxlength":50}""".trimMargin()
        val data = json.decodeFromString<FormLineData>(raw) as TextLineData

        assertEquals("username", data.name)
        assertEquals("Username", data.label)
        assertEquals("alice", data.value?.jsonPrimitive?.contentOrNull)
        assertEquals("required", data.validation)
        assertTrue(data.required)
        assertFalse(data.invalid)
        assertTrue(data.isLoginCredential == true)
        assertEquals("Enter name", data.placeholder)
        assertEquals("email", data.overrideType)
        assertEquals(50, data.maxLength)
    }

    @Test
    fun `deserialise TextLineData minimal fields`() {
        val raw = """{"type":"text","name":"field1"}"""
        val data = json.decodeFromString<FormLineData>(raw) as TextLineData

        assertEquals("field1", data.name)
        assertNull(data.label)
        assertNull(data.value)
        assertFalse(data.required)
    }

    @Test
    fun `TextLineData required is false when validation is absent`() {
        val raw = """{"type":"text","name":"f"}"""
        val data = json.decodeFromString<FormLineData>(raw) as TextLineData
        assertFalse(data.required)
    }

    @Test
    fun `TextLineData required is true when validation contains required`() {
        val raw = """{"type":"text","name":"f","validation":"required|minlength:3"}"""
        val data = json.decodeFromString<FormLineData>(raw) as TextLineData
        assertTrue(data.required)
    }

    // --- PasswordLineData ---

    @Test
    fun `deserialise PasswordLineData`() {
        val raw = """{"type":"password","name":"pin","label":"PIN","login_credential":true}"""
        val data = json.decodeFromString<FormLineData>(raw) as PasswordLineData

        assertEquals("pin", data.name)
        assertEquals("PIN", data.label)
        assertTrue(data.isLoginCredential == true)
    }

    // --- CheckBoxLineData ---

    @Test
    fun `deserialise CheckBoxLineData checked true`() {
        val raw = """{"type":"checkbox","name":"consent","label":"I agree","checked":true}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertEquals("consent", data.name)
        assertTrue(data.value?.jsonPrimitive?.boolean == true)
    }

    @Test
    fun `deserialise CheckBoxLineData checked false`() {
        val raw = """{"type":"checkbox","name":"consent","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertFalse(data.value?.jsonPrimitive?.boolean == true)
    }

    @Test
    fun `deserialise CheckBoxLineData privacy_policy sets isLoginCredential`() {
        val raw = """{"type":"checkbox","name":"privacy_policy","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        // CheckBoxLineDataSerializer hardcodes isLoginCredential = (name == "privacy_policy")
        assertTrue(data.isLoginCredential == true)
    }

    @Test
    fun `deserialise CheckBoxLineData non-privacy_policy has isLoginCredential false`() {
        val raw = """{"type":"checkbox","name":"other_consent","checked":false}"""
        val data = json.decodeFromString<FormLineData>(raw) as CheckBoxLineData

        assertFalse(data.isLoginCredential == true)
    }

    // --- RadioLineData ---

    @Test
    fun `deserialise RadioLineData`() {
        val raw = """{"type":"radio","name":"choice","checked":"option_a",
            |"options":["option_a","option_b"]}""".trimMargin()
        val data = json.decodeFromString<FormLineData>(raw) as RadioLineData

        assertEquals("choice", data.name)
        assertEquals("option_a", data.value?.jsonPrimitive?.contentOrNull)
        assertEquals(2, data.options.size)
    }

    // --- SelectLineData ---

    @Test
    fun `deserialise SelectLineData`() {
        val raw = """{"type":"select","name":"country","selected":"DE",
            |"options":{"DE":"Germany","EN":"United Kingdom"}}""".trimMargin()
        val data = json.decodeFromString<FormLineData>(raw) as SelectLineData

        assertEquals("country", data.name)
        assertEquals("DE", data.value?.jsonPrimitive?.contentOrNull)
    }

    // --- CaptchaLineData ---

    @Test
    fun `deserialise CaptchaLineData`() {
        val raw = """{"type":"captcha","name":"captcha","placeholder":"Enter code",
            |"data":"base64imagedata"}""".trimMargin()
        val data = json.decodeFromString<FormLineData>(raw) as CaptchaLineData

        assertEquals("captcha", data.name)
        assertEquals("Enter code", data.placeholder)
        assertEquals("base64imagedata", data.data)
    }

    // --- FlickerLineData ---

    @Test
    fun `deserialise FlickerLineData`() {
        val raw = """{"type":"flicker","name":"tan","code":[[1,0],[0,1],[1,1]]}"""
        val data = json.decodeFromString<FormLineData>(raw) as FlickerLineData

        assertEquals("tan", data.name)
        assertEquals(3, data.code.size)
        assertEquals(listOf(1, 0), data.code[0])
    }

    // --- HiddenLineData ---

    @Test
    fun `deserialise HiddenLineData`() {
        val raw = """{"type":"hidden","name":"token","value":"secret123"}"""
        val data = json.decodeFromString<FormLineData>(raw) as HiddenLineData

        assertEquals("token", data.name)
        assertEquals("secret123", data.value?.jsonPrimitive?.contentOrNull)
    }

    // --- SubmitLineData ---

    @Test
    fun `deserialise SubmitLineData with back label`() {
        val raw = """{"type":"submit","label":"Next","back":"Go back"}"""
        val data = json.decodeFromString<FormLineData>(raw) as SubmitLineData

        assertEquals("Next", data.label)
        assertEquals("Go back", data.backLabel)
    }

    @Test
    fun `deserialise SubmitLineData without back label`() {
        val raw = """{"type":"submit","label":"Submit"}"""
        val data = json.decodeFromString<FormLineData>(raw) as SubmitLineData

        assertEquals("Submit", data.label)
        assertNull(data.backLabel)
    }

    // --- AbortLineData ---

    @Test
    fun `deserialise AbortLineData`() {
        val raw = """{"type":"abort","label":"Cancel"}"""
        val data = json.decodeFromString<FormLineData>(raw) as AbortLineData
        assertEquals("Cancel", data.label)
    }

    // --- RestartLineData ---

    @Test
    fun `deserialise RestartLineData`() {
        val raw = """{"type":"restart","label":"Start over"}"""
        val data = json.decodeFromString<FormLineData>(raw) as RestartLineData
        assertEquals("Start over", data.label)
    }

    // --- TabsLineData ---

    @Test
    fun `deserialise TabsLineData`() {
        val raw = """{"type":"tabs","action":"switch","selected":"tab1",
            |"tabs":{"tab1":"First tab","tab2":"Second tab"}}""".trimMargin()
        val data = json.decodeFromString<FormLineData>(raw) as TabsLineData

        assertEquals("switch", data.action)
        assertEquals("tab1", data.selected)
        assertEquals(2, data.tabs.size)
        assertEquals("First tab", data.tabs["tab1"])
    }

    // --- ImageLineData ---

    @Test
    fun `deserialise ImageLineData`() {
        val raw = """{"type":"image","data":"base64==","align":"center","description":"QR code"}"""
        val data = json.decodeFromString<FormLineData>(raw) as ImageLineData

        assertEquals("base64==", data.data)
        assertEquals("center", data.align)
        assertEquals("QR code", data.description)
    }

    // --- LogoLineData ---

    @Test
    fun `deserialise LogoLineData`() {
        val raw = """{"type":"logo","tooltip":"Bank logo","logo_variation":"white"}"""
        val data = json.decodeFromString<FormLineData>(raw) as LogoLineData

        assertEquals("Bank logo", data.tooltip)
        assertEquals("white", data.logoVariation)
    }

    // --- DescriptionLineData ---

    @Test
    fun `deserialise DescriptionLineData`() {
        val raw = """{"type":"description","text":"Please enter your PIN."}"""
        val data = json.decodeFromString<FormLineData>(raw) as DescriptionLineData
        assertEquals("Please enter your PIN.", data.text)
    }

    // --- ParagraphLineData ---

    @Test
    fun `deserialise ParagraphLineData with severity`() {
        val raw = """{"type":"paragraph","text":"Invalid PIN","title":"Error","severity":"error"}"""
        val data = json.decodeFromString<FormLineData>(raw) as ParagraphLineData

        assertEquals("Invalid PIN", data.text)
        assertEquals("Error", data.title)
        assertEquals("error", data.severity)
    }

    // --- RedirectLineData ---

    @Test
    fun `deserialise RedirectLineData`() {
        val raw = """{"type":"redirect","name":"bank_redirect","label":"Continue to bank",
            |"back":"Go back","url":"https://bank.example.com/auth"}""".trimMargin()
        val data = json.decodeFromString<FormLineData>(raw) as RedirectLineData

        assertEquals("bank_redirect", data.name)
        assertEquals("Continue to bank", data.label)
        assertEquals("Go back", data.backLabel)
        assertEquals("https://bank.example.com/auth", data.url)
    }

    // --- AutoSubmitLineData ---

    @Test
    fun `deserialise AutoSubmitLineData`() {
        val raw = """{"type":"autosubmit","interval":3}"""
        val data = json.decodeFromString<FormLineData>(raw) as AutoSubmitLineData
        assertEquals(3, data.interval)
    }

    // --- unknown fields ---

    @Test
    fun `deserialise ignores unknown fields`() {
        val raw = """{"type":"text","name":"f","unknown_field":"ignored","another":42}"""
        val data = json.decodeFromString<FormLineData>(raw) as TextLineData
        assertEquals("f", data.name)
    }

    // --- CredentialFormLineData.getProviderName ---

    @Test
    fun `getProviderName combines provider and field name`() {
        val raw = """{"type":"text","name":"login","login_credential":true}"""
        val data = json.decodeFromString<FormLineData>(raw) as TextLineData
        assertEquals("mybank_login", data.getProviderName("mybank"))
    }
}
