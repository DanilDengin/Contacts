package com.example.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.widget.AppCompatImageView
import com.example.ui.R
import com.example.utils.tag.tagObj

class CircleCatProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
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
                        rotateAnimationDurationDefault
                    ).coerceIn(rotateAnimationDurationMin, rotateAnimationDurationMax)
                } catch (e: Throwable) {
                    Log.e(this.tagObj(), "Can't apply attributes", e)
                } finally {
                    recycle()
                }
            }
    }

    private fun startAnimation() {
        clearAnimation()
        val rotate = RotateAnimation(
            360f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
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
        const val rotateAnimationDurationDefault = 1400
        const val rotateAnimationDurationMin = 600
        const val rotateAnimationDurationMax = 2800
    }
}