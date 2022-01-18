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
        }

    override fun serialize(encoder: Encoder, value: CheckBoxLineData) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeStringElement(descriptor, 1, value.label.toString())
            encodeBooleanElement(descriptor, 2, value.value?.jsonPrimitive?.booleanOrNull ?: false)
            encodeBooleanElement(descriptor, 3, value.disabled ?: false)
        }

    override fun deserialize(decoder: Decoder): CheckBoxLineData =
        decoder.decodeStructure(descriptor) {
            var name = ""
            var label: String? = null
            var value = false
            var disabled = false

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> label = decodeStringElement(descriptor, index)
                    2 -> value = decodeBooleanElement(descriptor, index)
                    3 -> disabled = decodeBooleanElement(descriptor, index)
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
                disabled
            )
        }
}