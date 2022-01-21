package com.fintecsystems.xs2awizard.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

object MarkupParser {
    private val markupRegex =
        Regex("\\[([\\w\\s\\-().'!?]+)(\\|(\\w+)(::([&=\\w:/\\\\.\\-_]+))?)?]")
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
    fun parseMarkupText(textToParse: String): AnnotatedString {
        // Replace HTML linebreak and middot with Kotlin String counterparts.
        val text = lineBreakRegex
            .replace(
                middotRegex.replace(textToParse, "\u00B7"),
                "\n",
            )

        return buildAnnotatedString {
            // Cursor used to determine startIndex of the current section.
            var cursor = 0

            do {
                // Search for markups beginning at the cursor.
                val regexResult = markupRegex.find(text, cursor)

                if (regexResult != null) {
                    // Append all text between the cursor and beginning of markup.
                    append(text.slice(cursor until regexResult.range.first))

                    // Define start of the annotation
                    val annotationType = regexResult.groupValues[3]
                    val isUrlAnnotation = annotationType in listOf("autosubmit", "link", "dialog")
                    val style =
                        if (isUrlAnnotation) {
                            when (annotationType) {
                                "autosubmit" -> pushStringAnnotation(
                                    tag = "autosubmit",
                                    annotation = regexResult.groupValues[5]
                                )
                                else -> pushStringAnnotation(
                                    tag = "URL",
                                    annotation = regexResult.groupValues[5]
                                )
                            }

                            SpanStyle(
                                color = XS2ATheme.CURRENT.tintColor,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            when (annotationType) {
                                "bold" -> SpanStyle(
                                    color = XS2ATheme.CURRENT.textColor,
                                    fontWeight = FontWeight.Bold
                                )
                                "italic" -> SpanStyle(
                                    color = XS2ATheme.CURRENT.textColor,
                                    fontStyle = FontStyle.Italic
                                )
                                else -> SpanStyle(
                                    color = XS2ATheme.CURRENT.textColor,
                                )
                            }
                        }

                    // Append textValue of markup and style it.
                    withStyle(
                        style = style
                    ) {
                        append(regexResult.groupValues[1])
                    }


                    // Define end of the annotation
                    if (isUrlAnnotation) pop()

                    cursor = regexResult.range.last + 1
                }
            } while (regexResult != null)

            // If there is still text left, append the remaining section.
            if (cursor < text.length) {
                append(text.slice(cursor until text.length))
            }
        }
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
}