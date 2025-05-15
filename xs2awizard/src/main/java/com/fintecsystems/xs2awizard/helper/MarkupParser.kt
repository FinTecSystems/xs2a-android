package com.fintecsystems.xs2awizard.helper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

object MarkupParser {
    private val markupRegex =
        Regex("\\[([\\w\\s\\-().'!?]+)(\\|(\\w+)(::([&=\\w:/\\\\.\\-_?]+))?)?]")
    private val lineBreakRegex = Regex("[<\\[]br[>\\]]")
    private val middotRegex = Regex("&middot;")
    private val autoSubmitPayloadRegex = Regex("(\\w+)=(\\w+)")

    /**
     * Parses provided text for markups.
     *
     * @param textToParse [String] to parse for markups.
     * @return parsed text as [AnnotatedString].
     */
    @Composable
    fun parseParseResult(
        parseResult: ParseResult,
        linkInteractionListener: LinkInteractionListener? = null
    ): AnnotatedString {
        return buildAnnotatedString {
            parseResult.items.forEach { parseResultItem ->
                when (parseResultItem) {
                    is ParseResult.Item.Text -> {
                        if (parseResultItem.spanStyle != null) {
                            withStyle(
                                parseResultItem.spanStyle
                            ) {
                                append(parseResultItem.text)
                            }
                        } else {
                            append(parseResultItem.text)
                        }
                    }

                    is ParseResult.Item.AutoSubmit -> {
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = parseResultItem.payload,
                                linkInteractionListener = linkInteractionListener,
                                styles = TextLinkStyles(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            )
                        ) {
                            append(parseResultItem.text)
                        }
                    }

                    is ParseResult.Item.Link -> {
                        withLink(
                            LinkAnnotation.Url(
                                url = parseResultItem.url,
                                styles = TextLinkStyles(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            )
                        ) {
                            append(parseResultItem.text)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun parseMarkupText(textToParse: String): ParseResult {
        // Replace HTML linebreak and middot with Kotlin String counterparts.
        val text = lineBreakRegex
            .replace(
                middotRegex.replace(textToParse, "\u00B7"),
                "\n",
            )

        val resultItems = mutableListOf<ParseResult.Item>()

        // Cursor used to determine startIndex of the current section.
        var cursor = 0

        do {
            // Search for markups beginning at the cursor.
            val regexResult = markupRegex.find(text, cursor)

            if (regexResult != null) {
                if (cursor != regexResult.range.first) {
                    // Append all text between the cursor and beginning of markup.
                    resultItems.add(
                        ParseResult.Item.Text(text.slice(cursor until regexResult.range.first))
                    )
                }

                // Define start of the annotation
                val annotationContent = regexResult.groupValues[1]
                val annotationType = regexResult.groupValues[3]
                val annotationParameter = regexResult.groupValues[5]
                val annotationHasParameter = annotationParameter.isNotEmpty()

                if (annotationHasParameter) {
                    if (annotationType == "autosubmit") {
                        resultItems.add(
                            ParseResult.Item.AutoSubmit(
                                text = annotationContent,
                                payload = annotationParameter
                            )
                        )
                    } else {
                        resultItems.add(
                            ParseResult.Item.Link(
                                text = annotationContent,
                                url = annotationParameter
                            )
                        )
                    }
                } else {
                    val spanStyle = when (annotationType) {
                        "bold" -> SpanStyle(
                            fontWeight = FontWeight.Bold
                        )

                        "italic" -> SpanStyle(
                            fontStyle = FontStyle.Italic
                        )

                        else -> SpanStyle()
                    }

                    // Append textValue of markup and style it.
                    resultItems.add(
                        ParseResult.Item.Text(
                            text = annotationContent,
                            spanStyle = spanStyle
                        )
                    )
                }

                cursor = regexResult.range.last + 1
            }
        } while (regexResult != null)

        // If there is still text left, append the remaining section.
        if (cursor < text.length) {
            resultItems.add(
                ParseResult.Item.Text(text.slice(cursor until text.length))
            )
        }

        return ParseResult(resultItems)
    }

    /**
     * Parses payload of an autosubmit annotation and retrieves it's data into an JsonObject
     *
     * @param payload to parse.
     *
     * @return Parsed payload as JsonObject
     */
    fun parseAutoSubmitPayloadAsJson(payload: String) = buildJsonObject {
        autoSubmitPayloadRegex.findAll(payload)
            .forEach {
                it.groupValues.let { group ->
                    put(group[1], JsonPrimitive(group[2]))
                }
            }
    }

    data class ParseResult(
        val items: List<Item>
    ) {
        fun getText() = items.joinToString(
            separator = "",
            transform = { it.text }
        )

        sealed class Item(
            val text: String
        ) {
            class Text(
                text: String,
                val spanStyle: SpanStyle? = null
            ) : Item(text)

            class Link(
                text: String,
                val url: String
            ) : Item(text)

            class AutoSubmit(
                text: String,
                val payload: String
            ) : Item(text)
        }
    }
}