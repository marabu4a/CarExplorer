package com.example.carexplorer.ui.fragment.webpage

import android.graphics.Bitmap

enum class CacheableImage(
    val suffixes: Set<String>,
    val mimeType: String,
    val compressFormat: Bitmap.CompressFormat,
) {

    JPG(setOf(JPG_FORMAT, JPEG_FORMAT), JPG_MIME_FORMAT, Bitmap.CompressFormat.JPEG),
    PNG(setOf(PNG_FORMAT), PNG_MIME_FORMAT, Bitmap.CompressFormat.PNG),
    WEBP(setOf(WEBP_FORMAT), WEBP_MIME_FORMAT, Bitmap.CompressFormat.WEBP);

    companion object {
        fun getMatchingImage(url: String): CacheableImage? = values().find { image ->
            image.suffixes.any { url.contains(".$it") }
        }
    }
}

private const val JPG_FORMAT = "jpg"
private const val JPEG_FORMAT = "jpeg"
private const val PNG_FORMAT = "png"
private const val WEBP_FORMAT = "webp"
private const val JPG_MIME_FORMAT = "image/jpg"
private const val PNG_MIME_FORMAT = "image/png"
private const val WEBP_MIME_FORMAT = "image/webp"