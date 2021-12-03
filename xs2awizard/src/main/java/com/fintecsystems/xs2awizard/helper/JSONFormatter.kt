package com.fintecsystems.xs2awizard.helper

import com.fintecsystems.xs2awizard.form.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

object JSONFormatter {
    // Serializer Module used to de-/serialize between json and our FormLineData classes.
    private val formLineDataModule = SerializersModule {
        polymorphic(FormLineData::class) {
            subclass(MultiLineData::class)
            subclass(TabsLineData::class)
            subclass(RestartLineData::class)
            subclass(AbortLineData::class)
            subclass(SubmitLineData::class)
            subclass(ImageLineData::class)
            subclass(LogoLineData::class)
            subclass(DescriptionLineData::class)
            subclass(ParagraphLineData::class)
            subclass(RedirectLineData::class)
            subclass(TextLineData::class)
            subclass(PasswordLineData::class)
            subclass(CaptchaLineData::class)
            subclass(FlickerLineData::class)
            subclass(HiddenLineData::class)
            subclass(CheckBoxLineData::class)
            subclass(RadioLineData::class)
            subclass(SelectLineData::class)
            subclass(AutoSubmitLineData::class)
        }
    }

    val formatter = Json {
        serializersModule = formLineDataModule; ignoreUnknownKeys = true; isLenient = true
    }
}