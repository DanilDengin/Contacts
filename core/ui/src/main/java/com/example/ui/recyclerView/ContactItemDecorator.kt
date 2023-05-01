package com.example.ui.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.view.fromPxToDp

class ContactItemDecorator : RecyclerView.ItemDecoration() {

    private val spaceDp: Int = 8.fromPxToDp()

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
                spaceDp
            } else {
                0
            }
    }
}
