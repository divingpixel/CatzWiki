package com.epikron.catzwiki.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListVerticalDecorator(
	private val spaceSizeResource: Int,
	private val extraSpaceInterval: Int = 0,
	private val addEnds: Boolean = false
) :
	RecyclerView.ItemDecoration() {
	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		val spaceSize = parent.resources.getDimensionPixelSize(spaceSizeResource)
		val childPosition = parent.getChildAdapterPosition(view)
		with(outRect) {
			when {
				extraSpaceInterval > 0 &&
					childPosition.rem(extraSpaceInterval) == 0 &&
					childPosition > 0 -> top = (spaceSize * 2)

				addEnds && childPosition == 0 -> top = spaceSize

				childPosition + 1 == parent.adapter?.itemCount -> {
					top = spaceSize
					bottom = spaceSize
				}

				childPosition > 0 -> top = spaceSize

				else -> 0
			}
		}
	}
}