package com.example.lessons.contactslist

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class ContactsListItemDecorator : RecyclerView.ItemDecoration() {
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
        outRect.bottom = spaceDp
    }
}