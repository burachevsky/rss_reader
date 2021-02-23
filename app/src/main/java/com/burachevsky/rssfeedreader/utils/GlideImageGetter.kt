package com.burachevsky.rssfeedreader.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class GlideImageGetter (
    private val textView: TextView
) : ImageGetter {

    private val context = textView.context

    override fun getDrawable(url: String): Drawable {
        val drawable = BitmapDrawablePlaceholder()

        Glide.with(context)
            .asBitmap()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(drawable)

        return drawable
    }

    private inner class BitmapDrawablePlaceholder :
        BitmapDrawable(
            context.resources,
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        ),
        Target<Bitmap> {

        var drawable: Drawable? = null
            set(value) {
                field = value

                val drawable = field ?: return

                val drawableWidth = drawable.intrinsicWidth
                val drawableHeight = drawable.intrinsicHeight
                val maxWidth = textView.measuredWidth
                if (drawableWidth > maxWidth) {
                    val calculatedHeight = maxWidth * drawableHeight / drawableWidth
                    drawable.setBounds(0, 0, maxWidth, calculatedHeight)
                    setBounds(0, 0, maxWidth, calculatedHeight)
                } else {
                    drawable.setBounds(0, 0, drawableWidth, drawableHeight)
                    setBounds(0, 0, drawableWidth, drawableHeight)
                }
                textView.text = textView.text
            }

        override fun draw(canvas: Canvas) {
            if (drawable != null) {
                drawable?.draw(canvas)
            }
        }

        override fun onLoadStarted(placeholderDrawable: Drawable?) {
            placeholderDrawable?.let { drawable = it }
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            errorDrawable?.let { drawable = it }
        }

        override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
            drawable = BitmapDrawable(context.resources, bitmap)
        }

        override fun onLoadCleared(placeholderDrawable: Drawable?) {
            placeholderDrawable?.let { drawable = it }
        }

        override fun getSize(cb: SizeReadyCallback) {
            textView.post {
                cb.onSizeReady(
                    textView.width,
                    textView.height
                )
            }
        }

        override fun removeCallback(cb: SizeReadyCallback) {}
        override fun setRequest(request: Request?) {}

        override fun getRequest(): Request? {
            return null
        }

        override fun onStart() {}
        override fun onStop() {}
        override fun onDestroy() {}
    }
}