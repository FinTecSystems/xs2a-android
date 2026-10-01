package com.fintecsystems.xs2awizard.screenshot.form

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.ImageLineData
import com.fintecsystems.xs2awizard.form.components.ImageLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import java.io.ByteArrayOutputStream

class ImageLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    // Generated programmatically so Robolectric's native BitmapFactory can round-trip the bytes.
    private val testImageBase64: String by lazy {
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    override val baseName = "image_line"

    @Composable
    override fun Content() = ImageLine(
        formData = ImageLineData(data = testImageBase64, align = "center", description = "Bank logo"),
    )

}
