package com.fintecsystems.xs2awizard.form.serializers

import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

object CheckBoxLineDataSerializer : KSerializer<CheckBoxLineData> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("checkbox") {
            element<String>("name")
            element<String?>("label")
            element<Boolean?>("checked")
            element<Boolean?>("disabled")
            element<String?>("validation_error")
            element<String?>("validation")
            element<Boolean>("invalid")
            element<Boolean>("required")
        }

    override fun serialize(encoder: Encoder, value: CheckBoxLineData) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeStringElement(descriptor, 1, value.label.toString())
            encodeBooleanElement(descriptor, 2, value.value?.jsonPrimitive?.booleanOrNull == true)
            encodeBooleanElement(descriptor, 3, value.disabled == true)
            encodeStringElement(descriptor, 4, value.validationError.toString())
            encodeStringElement(descriptor, 5, value.validation.toString())
            encodeBooleanElement(descriptor, 6, value.invalid)
            encodeBooleanElement(descriptor, 7, value.required)
        }

    override fun deserialize(decoder: Decoder): CheckBoxLineData =
        decoder.decodeStructure(descriptor) {
            var name = ""
            var label: String? = null
            var value = false
            var disabled = false
            var validationError: String? = null
            var validation: String? = null
            var invalid = false
            var required = false

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> label = decodeStringElement(descriptor, index)
                    2 -> value = decodeBooleanElement(descriptor, index)
                    3 -> disabled = decodeBooleanElement(descriptor, index)
                    4 -> validationError = decodeStringElement(descriptor, index)
                    5 -> validation = decodeStringElement(descriptor, index)
                    6 -> invalid = decodeBooleanElement(descriptor, index)
                    7 -> required = decodeBooleanElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            require(name.isNotEmpty())

            CheckBoxLineData(
                name,
                label,
                name == "privacy_policy",
                JsonPrimitive(value),
                validationError,
                validation,
                invalid,
                required,
                disabled
            )
        }
}