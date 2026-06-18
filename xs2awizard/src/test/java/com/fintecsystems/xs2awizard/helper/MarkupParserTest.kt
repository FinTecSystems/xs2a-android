package com.fintecsystems.xs2awizard.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkupParserTest {

    // --- parseAutoSubmitPayloadAsJson ---

    @Test
    fun `parseAutoSubmitPayloadAsJson parses single key-value pair`() {
        val result = MarkupParser.parseAutoSubmitPayloadAsJson("action=submit")

        assertEquals("submit", result["action"]?.toString()?.trim('"'))
    }

    @Test
    fun `parseAutoSubmitPayloadAsJson parses multiple key-value pairs`() {
        val result = MarkupParser.parseAutoSubmitPayloadAsJson("action=submit&tab=online")

        assertNotNull(result["action"])
        assertNotNull(result["tab"])
        assertEquals("submit", result["action"]?.toString()?.trim('"'))
        assertEquals("online", result["tab"]?.toString()?.trim('"'))
    }

    @Test
    fun `parseAutoSubmitPayloadAsJson returns empty object for empty string`() {
        val result = MarkupParser.parseAutoSubmitPayloadAsJson("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseAutoSubmitPayloadAsJson returns empty object for no matches`() {
        val result = MarkupParser.parseAutoSubmitPayloadAsJson("no-pairs-here")
        assertTrue(result.isEmpty())
    }

    // --- parseMarkupText ---

    @Test
    fun `parseMarkupText returns plain text unchanged`() {
        val result = MarkupParser.parseMarkupText("Hello World")

        assertEquals("Hello World", result.getText())
        assertEquals(1, result.items.size)
        assertTrue(result.items[0] is MarkupParser.ParseResult.Item.Text)
    }

    @Test
    fun `parseMarkupText replaces HTML line break with newline`() {
        val result = MarkupParser.parseMarkupText("Line one<br>Line two")
        assertTrue(result.getText().contains("\n"))
    }

    @Test
    fun `parseMarkupText replaces bracket line break with newline`() {
        val result = MarkupParser.parseMarkupText("Line one[br]Line two")
        assertTrue(result.getText().contains("\n"))
    }

    @Test
    fun `parseMarkupText replaces middot entity with unicode middot`() {
        val result = MarkupParser.parseMarkupText("a &middot; b")
        assertTrue(result.getText().contains("\u00B7"))
    }

    @Test
    fun `parseMarkupText parses link annotation`() {
        val result = MarkupParser.parseMarkupText("[Click here|link::https://example.com]")

        assertEquals(1, result.items.size)
        val item = result.items[0]
        assertTrue(item is MarkupParser.ParseResult.Item.Link)
        assertEquals("Click here", item.text)
        assertEquals("https://example.com", (item as MarkupParser.ParseResult.Item.Link).url)
    }

    @Test
    fun `parseMarkupText parses autosubmit annotation`() {
        val result = MarkupParser.parseMarkupText("[Proceed|autosubmit::action=next]")

        assertEquals(1, result.items.size)
        val item = result.items[0]
        assertTrue(item is MarkupParser.ParseResult.Item.AutoSubmit)
        assertEquals("Proceed", item.text)
        assertEquals("action=next", (item as MarkupParser.ParseResult.Item.AutoSubmit).payload)
    }

    @Test
    fun `parseMarkupText parses bold annotation`() {
        val result = MarkupParser.parseMarkupText("[Important|bold]")

        assertEquals(1, result.items.size)
        val item = result.items[0]
        assertTrue(item is MarkupParser.ParseResult.Item.Text)
        assertNotNull((item as MarkupParser.ParseResult.Item.Text).spanStyle)
    }

    @Test
    fun `parseMarkupText parses italic annotation`() {
        val result = MarkupParser.parseMarkupText("[Note|italic]")

        assertEquals(1, result.items.size)
        val item = result.items[0]
        assertTrue(item is MarkupParser.ParseResult.Item.Text)
        assertNotNull((item as MarkupParser.ParseResult.Item.Text).spanStyle)
    }

    @Test
    fun `parseMarkupText preserves text before and after annotation`() {
        val result = MarkupParser.parseMarkupText("Before [link text|link::https://x.com] after")

        assertEquals(3, result.items.size)
        assertTrue(result.items[0] is MarkupParser.ParseResult.Item.Text)
        assertTrue(result.items[1] is MarkupParser.ParseResult.Item.Link)
        assertTrue(result.items[2] is MarkupParser.ParseResult.Item.Text)
        assertEquals("Before ", result.items[0].text)
        assertEquals(" after", result.items[2].text)
    }

    @Test
    fun `parseMarkupText handles multiple annotations`() {
        val result = MarkupParser.parseMarkupText(
            "[First link|link::https://a.com] and [Second link|link::https://b.com]"
        )

        assertEquals(3, result.items.size)
        assertTrue(result.items[0] is MarkupParser.ParseResult.Item.Link)
        assertTrue(result.items[1] is MarkupParser.ParseResult.Item.Text)
        assertTrue(result.items[2] is MarkupParser.ParseResult.Item.Link)
    }

    @Test
    fun `ParseResult getText concatenates all item texts`() {
        val result = MarkupParser.parseMarkupText("[Bold|bold] text [link|link::https://x.com]")
        assertEquals("Bold text link", result.getText())
    }

    @Test
    fun `parseMarkupText handles text with no annotations`() {
        val input = "Just a plain description with no markup."
        val result = MarkupParser.parseMarkupText(input)

        assertEquals(1, result.items.size)
        assertEquals(input, result.getText())
    }

    @Test
    fun `parseMarkupText handles empty string`() {
        val result = MarkupParser.parseMarkupText("")
        assertEquals(0, result.items.size)
        assertEquals("", result.getText())
    }

    // --- ParseResult.getText ---

    @Test
    fun `ParseResult getText on empty items returns empty string`() {
        val result = MarkupParser.ParseResult(emptyList())
        assertEquals("", result.getText())
    }

    // --- Comma parsing ---

    @Test
    fun `parseMarkupText parses annotation text containing a comma`() {
        val result = MarkupParser.parseMarkupText("[Yes, I agree|bold]")

        assertEquals(1, result.items.size)
        val item = result.items[0]
        assertTrue(item is MarkupParser.ParseResult.Item.Text)
        assertEquals("Yes, I agree", item.text)
    }

    @Test
    fun `parseMarkupText parses link annotation with comma in label`() {
        val result = MarkupParser.parseMarkupText("[Terms, Privacy|link::https://example.com]")

        assertEquals(1, result.items.size)
        val item = result.items[0]
        assertTrue(item is MarkupParser.ParseResult.Item.Link)
        assertEquals("Terms, Privacy", item.text)
        assertEquals("https://example.com", (item as MarkupParser.ParseResult.Item.Link).url)
    }

    @Test
    fun `parseMarkupText handles plain text with commas unchanged`() {
        val input = "Hello, World!"
        val result = MarkupParser.parseMarkupText(input)

        assertEquals(1, result.items.size)
        assertEquals(input, result.getText())
    }

    @Test
    fun `parseMarkupText parses mixed content where annotation label contains comma`() {
        val result = MarkupParser.parseMarkupText("Please read [our terms, conditions|link::https://example.com] carefully.")

        assertEquals(3, result.items.size)
        assertTrue(result.items[0] is MarkupParser.ParseResult.Item.Text)
        assertTrue(result.items[1] is MarkupParser.ParseResult.Item.Link)
        assertTrue(result.items[2] is MarkupParser.ParseResult.Item.Text)
        assertEquals("our terms, conditions", result.items[1].text)
    }
}
