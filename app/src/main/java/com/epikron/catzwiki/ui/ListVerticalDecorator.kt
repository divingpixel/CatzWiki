package com.epikron.catzwiki.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListVerticalDecorator(
    private val spaceSizeResource: Int,
    private val extraSpaceInterval: Int = 0,
    private val addEnds: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spaceSize = parent.resources.getDimensionPixelSize(spaceSizeResource)
        val childPosition = parent.getChildAdapterPosition(view)
        var topValue = 0
        var bottomValue = 0
        if (extraSpaceInterval > 0 && childPosition.rem(extraSpaceInterval) == 0 && childPosition > 0)
            topValue += spaceSize
        if (addEnds && childPosition == 0) topValue += spaceSize * 2
        if (addEnds && childPosition + 1 == parent.adapter?.itemCount) {
            topValue = spaceSize
            bottomValue = spaceSize * 2
        }
        if (childPosition > 0 && childPosition < parent.adapter?.itemCount ?: 1) topValue = spaceSize
        with(outRect) {
            top = topValue
            bottom = bottomValue
        }
    }

}
