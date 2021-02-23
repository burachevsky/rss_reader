package com.burachevsky.rssfeedreader.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.*
import com.burachevsky.rssfeedreader.R


class LikeButton constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, R.attr.imageButtonStyle)

    var onLikeListener: ((Boolean) -> Unit)? = null

    var likeImage: Int = 0

    var unlikeImage: Int = 0

    private var _isLiked = false
    var isLiked: Boolean
        get() = _isLiked
        set(value) {
            _isLiked = value
            setImageResource(if (value) likeImage else unlikeImage)
            onLikeListener?.invoke(value)
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LikeButton,
            0, 0
        ).apply {
            try {
                likeImage = getResourceId(R.styleable.LikeButton_likeImage, 0)
                unlikeImage = getResourceId(R.styleable.LikeButton_unlikeImage, 0)
            } finally {
                recycle()
            }
        }

        isLiked = false

        setOnClickListener {
            isLiked = !isLiked
        }
    }

    fun init(value: Boolean/*, onLiked: ((Boolean) -> Unit)?*/) {
        _isLiked = value
        setImageResource(if (value) likeImage else unlikeImage)
        /*onLikeListener = null
        isLiked = value
        onLikeListener = onLiked*/
    }
}

