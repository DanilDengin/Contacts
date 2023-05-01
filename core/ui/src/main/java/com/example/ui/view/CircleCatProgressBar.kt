package com.example.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.widget.AppCompatImageView
import com.example.ui.R

class CircleCatProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var rotateAnimationDuration: Int? = null

    init {
        attrs?.also(::initAttrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageResource(R.drawable.ic_cat_progress_bar)
        startAnimation()
    }

    private fun initAttrs(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CircleCatProgressBar)
            .apply {
                try {
                    rotateAnimationDuration = getInteger(
                        R.styleable.CircleCatProgressBar_animationDurationCCPBmsec,
                        ROTATE_ANIMATION_DURATION_DEFAULT
                    ).coerceIn(ROTATE_ANIMATION_DURATION_MIN, ROTATE_ANIMATION_DURATION_MAX)
                } finally {
                    recycle()
                }
            }
    }

    private fun startAnimation() {
        clearAnimation()
        val rotate = RotateAnimation(
            ROTATE_ANIMATION_FROM_DEGREES,
            ROTATE_ANIMATION_TO_DEGREES,
            Animation.RELATIVE_TO_SELF,
            ANIMATION_PIVOT,
            Animation.RELATIVE_TO_SELF,
            ANIMATION_PIVOT
        ).apply {
            rotateAnimationDuration?.also { duration = it.toLong() }
            repeatCount = Animation.INFINITE
        }
        startAnimation(rotate)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            startAnimation()
        } else {
            clearAnimation()
        }
    }

    private companion object {
        const val ROTATE_ANIMATION_DURATION_DEFAULT = 1400
        const val ROTATE_ANIMATION_DURATION_MIN = 600
        const val ROTATE_ANIMATION_DURATION_MAX = 2800
        const val ROTATE_ANIMATION_FROM_DEGREES = 360f
        const val ROTATE_ANIMATION_TO_DEGREES = 0f
        const val ANIMATION_PIVOT = 0.5f
    }
}
