package com.fintecsystems.xs2awizard.helper

import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UtilsTest {

    // --- checkIfLanguageNeedsToBeChanged ---

    @Test
    fun `returns false when language is null`() {
        // null language means "use device default", no change needed
        assertFalse(Utils.checkIfLanguageNeedsToBeChanged(null))
    }

    @Test
    fun `returns false when language matches target`() {
        assertFalse(
            Utils.checkIfLanguageNeedsToBeChanged(
                language = XS2AWizardLanguage.DE,
                targetLanguage = XS2AWizardLanguage.DE,
            )
        )
    }

    @Test
    fun `returns true when supported target language differs from current language`() {
        assertTrue(
            Utils.checkIfLanguageNeedsToBeChanged(
                language = XS2AWizardLanguage.EN,
                targetLanguage = XS2AWizardLanguage.DE,
            )
        )
    }

    @Test
    fun `returns true when current is DE and target is EN`() {
        assertTrue(
            Utils.checkIfLanguageNeedsToBeChanged(
                language = XS2AWizardLanguage.DE,
                targetLanguage = XS2AWizardLanguage.EN,
            )
        )
    }

    @Suppress("DEPRECATION")
    @Test
    fun `returns false when target language is unsupported deprecated FR`() {
        // FR is not supported, so no change should be requested
        assertFalse(
            Utils.checkIfLanguageNeedsToBeChanged(
                language = XS2AWizardLanguage.EN,
                targetLanguage = XS2AWizardLanguage.FR,
            )
        )
    }
}
