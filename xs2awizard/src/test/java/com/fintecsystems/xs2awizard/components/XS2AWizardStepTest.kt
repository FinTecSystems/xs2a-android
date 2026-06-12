package com.fintecsystems.xs2awizard.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class XS2AWizardStepTest {

    @Test
    fun `getRelevantStep returns Tan for tan`() {
        val step = XS2AWizardStep.getRelevantStep("tan")
        assertEquals("tan", step?.stepName)
        assert(step is XS2AWizardStep.Tan)
    }

    @Test
    fun `getRelevantStep returns Bank for bank`() {
        val step = XS2AWizardStep.getRelevantStep("bank")
        assertEquals("bank", step?.stepName)
        assert(step is XS2AWizardStep.Bank)
    }

    @Test
    fun `getRelevantStep returns Account for account`() {
        val step = XS2AWizardStep.getRelevantStep("account")
        assertEquals("account", step?.stepName)
        assert(step is XS2AWizardStep.Account)
    }

    @Test
    fun `getRelevantStep returns Login for login`() {
        val step = XS2AWizardStep.getRelevantStep("login")
        assertEquals("login", step?.stepName)
        assert(step is XS2AWizardStep.Login)
    }

    @Test
    fun `getRelevantStep returns Other for unknown step name`() {
        val step = XS2AWizardStep.getRelevantStep("custom_step")
        assertEquals("custom_step", step?.stepName)
        assert(step is XS2AWizardStep.Other)
    }

    @Test
    fun `getRelevantStep returns null for null input`() {
        assertNull(XS2AWizardStep.getRelevantStep(null))
    }
}
