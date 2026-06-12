package com.fintecsystems.xs2awizard.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class XS2AWizardLanguageTest {

    // --- fromString ---

    @Test
    fun `fromString returns DE for lowercase de`() {
        assertEquals(XS2AWizardLanguage.DE, XS2AWizardLanguage.fromString("de"))
    }

    @Test
    fun `fromString returns EN for lowercase en`() {
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.fromString("en"))
    }

    @Test
    fun `fromString is case-insensitive`() {
        assertEquals(XS2AWizardLanguage.DE, XS2AWizardLanguage.fromString("DE"))
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.fromString("En"))
    }

    @Test
    fun `fromString returns default EN for unknown value`() {
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.fromString("xx"))
    }

    @Test
    fun `fromString returns custom default for unknown value`() {
        assertEquals(XS2AWizardLanguage.DE, XS2AWizardLanguage.fromString("xx", XS2AWizardLanguage.DE))
    }

    @Test
    fun `fromString returns empty string default`() {
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.fromString(""))
    }

    // --- isSupported ---

    @Test
    fun `isSupported returns true for DE`() {
        assertTrue(XS2AWizardLanguage.isSupported(XS2AWizardLanguage.DE))
    }

    @Test
    fun `isSupported returns true for EN`() {
        assertTrue(XS2AWizardLanguage.isSupported(XS2AWizardLanguage.EN))
    }

    @Suppress("DEPRECATION")
    @Test
    fun `isSupported returns false for deprecated FR`() {
        assertFalse(XS2AWizardLanguage.isSupported(XS2AWizardLanguage.FR))
    }

    @Suppress("DEPRECATION")
    @Test
    fun `isSupported returns false for deprecated IT`() {
        assertFalse(XS2AWizardLanguage.isSupported(XS2AWizardLanguage.IT))
    }

    @Suppress("DEPRECATION")
    @Test
    fun `isSupported returns false for deprecated ES`() {
        assertFalse(XS2AWizardLanguage.isSupported(XS2AWizardLanguage.ES))
    }

    // --- conformToSupportedLanguage ---

    @Test
    fun `conformToSupportedLanguage returns self when supported`() {
        assertEquals(XS2AWizardLanguage.DE, XS2AWizardLanguage.DE.conformToSupportedLanguage())
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.EN.conformToSupportedLanguage())
    }

    @Suppress("DEPRECATION")
    @Test
    fun `conformToSupportedLanguage falls back to EN for deprecated FR`() {
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.FR.conformToSupportedLanguage())
    }

    @Suppress("DEPRECATION")
    @Test
    fun `conformToSupportedLanguage falls back to EN for deprecated IT`() {
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.IT.conformToSupportedLanguage())
    }

    @Suppress("DEPRECATION")
    @Test
    fun `conformToSupportedLanguage falls back to EN for deprecated ES`() {
        assertEquals(XS2AWizardLanguage.EN, XS2AWizardLanguage.ES.conformToSupportedLanguage())
    }
}
