package com.fintecsystems.xs2awizard.form

import com.fintecsystems.xs2awizard.form.serializers.CheckBoxLineDataSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * BaseClass for the data of all form lines.
 */
@Polymorphic
@Serializable
abstract class FormLineData

/**
 * [FormLineData] extension for form lines, that display a label.
 */
@Polymorphic
@Serializable
abstract class LabelFormLineData : FormLineData() {
    abstract val label: String?
}

/**
 * [LabelFormLineData] extension for form lines, that store a value.
 */
@Polymorphic
@Serializable
abstract class ValueFormLineData : LabelFormLineData() {
    abstract val name: String
    abstract var value: JsonElement?
}

/**
 * [ValueFormLineData] extension for form lines, that could possibly be login-credentials.
 */
@Polymorphic
@Serializable
abstract class CredentialFormLineData : ValueFormLineData() {
    @SerialName("login_credential")
    abstract val isLoginCredential: Boolean?

    fun getProviderName(provider: String) = "${provider}_$name"
}

@SerialName("tabs")
@Serializable
class TabsLineData(
    val action: String,
    val selected: String,
    val tabs: Map<String, String>
) : FormLineData()

@SerialName("restart")
@Serializable
class RestartLineData(
    override val label: String?
) : LabelFormLineData()

@SerialName("abort")
@Serializable
class AbortLineData(
    override val label: String?
) : LabelFormLineData()

@SerialName("submit")
@Serializable
class SubmitLineData(
    override val label: String?,
    @SerialName("back")
    val backLabel: String? = null,
) : LabelFormLineData()

@SerialName("image")
@Serializable
class ImageLineData(
    val data: String,
    val align: String,
) : FormLineData()

@SerialName("logo")
@Serializable
class LogoLineData(
    val tooltip: String? = null,
    @SerialName("logo_variation")
    val logoVariation: String? = null,
) : FormLineData()

@SerialName("description")
@Serializable
class DescriptionLineData(
    val text: String? = null,
) : FormLineData()

@SerialName("paragraph")
@Serializable
class ParagraphLineData(
    val text: String,
    val title: String? = null,
    val severity: String? = null,
) : FormLineData()

@SerialName("redirect")
@Serializable
class RedirectLineData(
    val name: String,
    override val label: String? = null,
    @SerialName("back")
    val backLabel: String? = null,
    val url: String? = null,
) : LabelFormLineData()

@SerialName("text")
@Serializable
class TextLineData(
    override val name: String,
    override val label: String? = null,
    override var value: JsonElement? = null,
    @SerialName("login_credential")
    override val isLoginCredential: Boolean? = false,
    val disabled: Boolean? = false,
    val placeholder: String? = null,
    @SerialName("autocomplete_action")
    val autoCompleteAction: String? = null,
    @SerialName("override_type")
    val overrideType: String? = null,
    @SerialName("maxlength")
    val maxLength: Int? = 0,
) : CredentialFormLineData()

@SerialName("password")
@Serializable
class PasswordLineData(
    override val name: String,
    override val label: String? = null,
    override var value: JsonElement? = null,
    @SerialName("login_credential")
    override val isLoginCredential: Boolean? = false,
    val placeholder: String? = null,
) : CredentialFormLineData()

@SerialName("captcha")
@Serializable
class CaptchaLineData(
    override val name: String,
    override val label: String? = null,
    override var value: JsonElement? = null,
    val placeholder: String,
    val data: String,
) : ValueFormLineData()

@SerialName("flicker")
@Serializable
class FlickerLineData(
    override val name: String,
    override val label: String? = null,
    override var value: JsonElement? = null,
    val placeholder: String? = null,
    val code: List<List<Int>>,
) : ValueFormLineData()

@SerialName("hidden")
@Serializable
class HiddenLineData(
    override val name: String,
    override val label: String? = null,
    override var value: JsonElement? = null,
) : ValueFormLineData()

@SerialName("checkbox")
@Serializable(with = CheckBoxLineDataSerializer::class)
class CheckBoxLineData(
    override val name: String,
    override val label: String? = null,
    @SerialName("login_credential")
    override val isLoginCredential: Boolean? = false,
    @SerialName("checked")
    override var value: JsonElement? = null,
    val disabled: Boolean? = false,
) : CredentialFormLineData()

@SerialName("radio")
@Serializable
class RadioLineData(
    override val name: String,
    override val label: String? = null,
    @SerialName("checked")
    override var value: JsonElement? = null,
    val options: List<JsonElement>,
) : ValueFormLineData()

@SerialName("select")
@Serializable
class SelectLineData(
    override val name: String,
    override val label: String? = null,
    @SerialName("selected")
    override var value: JsonElement? = null,
    val options: JsonElement,
) : ValueFormLineData()

@SerialName("autosubmit")
@Serializable
class AutoSubmitLineData(
    val interval: Int,
) : FormLineData()
