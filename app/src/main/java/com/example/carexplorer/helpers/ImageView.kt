package com.example.carexplorer.helpers

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.color
import com.google.android.gms.common.util.zzc.isMainThread
import timber.log.Timber
import kotlin.math.max

val defaultStrategy: DiskCacheStrategy = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
    DiskCacheStrategy.ALL
} else DiskCacheStrategy.AUTOMATIC

fun ImageView.setImageUrl(
    url: String?,
    @DrawableRes error: Int? = R.drawable.gradient,
    afterImageSet: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    onBitmapCallback: ((Bitmap) -> Unit)? = null,
) {
    /**
     * if we measure view with image it will run in background thread
     * we don't need images for it, so we turn off it
     */
    if (!isMainThread()) return
    if (url.isNullOrBlank()) {
        error?.let { setImageResource(it) }
        return
    }
    try {
        Glide.with(context)
            .load(Uri.parse(url)).apply {
                if (error != null) {
                    error(error)
                }
            }
            .diskCacheStrategy(defaultStrategy)
            .intoWithCallbacks(
                imageView = this,
                doAfter = afterImageSet,
                onError = onError,
                block = { drawable ->
                    onBitmapCallback?.invoke(drawable.toBitmap())
                }
            )
    } catch (e: IllegalArgumentException) {
        Timber.e(e)
    }
}

fun ImageView.setCircleImageUrl(
    url: String,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    setCircleImageUri(Uri.parse(url), placeholder, error)
}

fun ImageView.setCircleImageUri(
    uri: Uri,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    /**
     * if we measure view with image it will run in background thread
     * we don't need images for it, so we turn off it
     */
    if (!isMainThread()) return

    Glide.with(context)
        .load(uri).apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
            if (error != null) {
                error(error)
            }
        }
        .diskCacheStrategy(defaultStrategy)
        .transition(DrawableTransitionOptions.withCrossFade())
        .circleCrop()
        .intoWithCallbacks(this)
}

fun ImageView.setCircleImageBitmap(bitmap: Bitmap) {
    /**
     * if we measure view with image it will run in background thread
     * we don't need images for it, so we turn off it
     */
    if (!isMainThread()) return

    Glide.with(context)
        .load(bitmap)
        .transition(DrawableTransitionOptions.withCrossFade())
        .circleCrop()
        .intoWithCallbacks(this)
}

fun RequestBuilder<Drawable>.intoWithCallbacks(
    imageView: ImageView,
    doAfter: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    block: (ImageViewTarget<Drawable>.(resource: Drawable) -> Unit)? = null
) {
    into<Target<Drawable>>(ImageDrawableTargetWithCallbacks(imageView, doAfter, onError, block))
}

class ImageDrawableTargetWithCallbacks(
    imageView: ImageView,
    private val doAfter: (() -> Unit)? = null,
    private val onError: (() -> Unit)? = null,
    private val block: (ImageViewTarget<Drawable>.(resource: Drawable) -> Unit)? = null
) : DrawableImageViewTarget(imageView) {
    override fun setDrawable(drawable: Drawable?) {
        // this need when placeholder is not set, but Glide whatever set null drawable
        if (drawable != null) {
            super.setDrawable(drawable)
            doAfter?.invoke()
        }
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        onError?.invoke()
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        block?.invoke(this, resource)
    }
}

fun ImageView.setGifImage(
    @DrawableRes res: Int
) {
    Glide.with(context)
        .asGif()
        .load(res)
        .into(this)
}

fun ImageView.setTintResources(@ColorRes colorRes: Int) {
    setTint(color(colorRes))
}

fun ImageView.setTint(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun ImageView.clearTint() {
    ImageViewCompat.setImageTintList(this, null)
    // need for android 5.0.2, see https://jira01.goldapple.ru/browse/APP-5072
    clearColorFilter()
}

/**
 * Set matrix scale type and matrix that identical to centerCrop
 * BE AWARE: use only when drawable is set
 */
fun ImageView.setCenterCropMatrix() {
    val drawable = drawable ?: return
    val contentWidth = drawable.intrinsicWidth
    val contentHeight = drawable.intrinsicHeight
    val viewWidth = measuredWidth.toFloat()
    val viewHeight = measuredHeight.toFloat()
    val scale = max(
        viewWidth / contentWidth,
        viewHeight / contentHeight
    )
    val matrix = Matrix()
    matrix.postScale(scale, scale)
    matrix.postTranslate(
        (viewWidth - contentWidth * scale) / 2,
        (viewHeight - contentHeight * scale) / 2
    )
    imageMatrix = matrix
}