package com.example.carexplorer.helpers

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class XGlideImageGetter(
    private val context: Context,
    private val textView: TextView,
    private val requestManager: RequestManager
) : Html.ImageGetter {
    override fun getDrawable(source: String?): Drawable {
        val holder = BitmapDrawablePlaceHolder(context.resources,null)
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val bitmap = requestManager
                    .asBitmap()
                    .load(source)
                    .submit()
                    .get()

                val drawable = BitmapDrawable(context.resources,bitmap)
                val scale = context.resources.displayMetrics.density

                val width = (drawable.intrinsicWidth * scale/2).roundToInt()
                val height = (drawable.intrinsicHeight * scale/2).roundToInt()
                drawable.setBounds(0, 0, width, height)

                holder.setDrawable(drawable)
                holder.setBounds(0, 0, width, height)
                withContext(Dispatchers.Main) {
                    textView.text = textView.text
                }
            }
        }
        return holder
    }

    internal class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) : BitmapDrawable(res, bitmap) {
        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
        }
    }
}