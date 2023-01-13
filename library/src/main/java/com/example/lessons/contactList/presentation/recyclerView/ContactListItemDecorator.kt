package com.example.lessons.contactList.presentation.recyclerView

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.utils.decoration.fromPxToDp
import kotlin.math.roundToInt

internal class ContactListItemDecorator : RecyclerView.ItemDecoration() {

    private val spacePx: Int =8

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val childAdapterPosition = parent.getChildAdapterPosition(view)
            .let { if (it == RecyclerView.NO_POSITION) return else it }
        val parentAdapterItemCount = parent.adapter?.itemCount?.minus(1) ?: -1
        outRect.bottom =
            if (childAdapterPosition != parentAdapterItemCount) {
                spacePx.fromPxToDp()
            } else {
                0
            }
    }
}