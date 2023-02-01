package com.example.lessons.utils.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.widget.AppCompatImageView
import com.example.library.R

class CircleCatProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatImageView(context, attrs) {

    private var rotateAnimationDuration: Int? = null

    init {
        attrs?.also(::initAttrs)
        isSaveEnabled = true
        isSaveFromParentEnabled = true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageResource(imageId)
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
                    Log.e(CIRCLE_CAT_PROGRESS_BAR_TAG, "Can't apply attributes", e)
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
        val imageId = R.drawable.ic_cat_progress_bar
        const val rotateAnimationDurationDefault = 1400
        const val rotateAnimationDurationMin = 600
        const val rotateAnimationDurationMax = 2800
        val CIRCLE_CAT_PROGRESS_BAR_TAG: String =
            CircleCatProgressBar::class.java.simpleName
    }
}