package com.example.lessons.contactlist

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class ContactListItemDecorator : RecyclerView.ItemDecoration() {
    private val spaceDp: Int = fromIntToDp(8)

    private fun fromIntToDp(spaceInt: Int): Int {
        return (spaceInt * Resources.getSystem().displayMetrics.density).roundToInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val childAdapterPosition = parent.getChildAdapterPosition(view)
            .let { if (it == RecyclerView.NO_POSITION) return else it }
        outRect.bottom =
            if (childAdapterPosition != (parent.adapter?.itemCount?.minus(1) ?: -1)) {
                spaceDp
            } else {
                0
            }
    }

}