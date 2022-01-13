package com.epikron.catzwiki.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.ViewSubtotalSectionBinding
import com.epikron.catzwiki.ui.adapters.SubtotalListViewAdapter

fun View.expand() {
	measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	val actualHeight = measuredHeight
	layoutParams.height = 0
	visibility = View.VISIBLE
	val animation = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
			layoutParams.height =
				if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
				else (actualHeight * interpolatedTime).toInt()
			requestLayout()
		}
	}
	animation.duration = (actualHeight / context.resources.displayMetrics.density).toLong()
	startAnimation(animation)
}

fun View.collapse() {
	val actualHeight = measuredHeight
	val animation = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
			if (interpolatedTime == 1f) {
				visibility = View.GONE
			} else {
				layoutParams.height = actualHeight - (actualHeight * interpolatedTime).toInt()
				requestLayout()
			}
		}
	}
	animation.duration = (actualHeight / context.resources.displayMetrics.density).toLong()
	startAnimation(animation)
}

data class SubtotalSectionViewData(
	val label: String? = null,
	val value: String? = null,
	val items: List<PriceLineViewData> = listOf()
)

class SubtotalSectionView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

	private val binding = ViewSubtotalSectionBinding.inflate(LayoutInflater.from(context),this)
	private val subtotalListViewAdapter = SubtotalListViewAdapter()
	private var isExpanded = false

	var state = SubtotalSectionViewData()
		set(value) {
			field = value
			setValues(value)
		}

	init {
		layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
		with(binding) {
			subtotalSectionList.isVisible = isExpanded
			subtotalSectionBackground.isVisible = isExpanded
			subtotalSectionHeaderButton.setImageResource(if(isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand)

			subtotalSectionList.run {
				layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
				adapter = subtotalListViewAdapter
			}

			subtotalSectionHeaderButton.setOnClickListener {
				isExpanded = !isExpanded
				if (isExpanded) subtotalSectionList.expand() else subtotalSectionList.collapse()
				subtotalSectionBackground.isVisible = isExpanded
				subtotalSectionHeaderButton.setImageResource(if(isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand)
			}

			subtotalSectionBackground.setOnClickListener {  }
		}
	}

	private fun setValues(values: SubtotalSectionViewData?) {
		with(binding) {
			values?.let { viewData ->
				subtotalSectionHeaderLabel.text = viewData.label
				subtotalSectionHeaderValue.text = viewData.value
				subtotalListViewAdapter.setData(viewData.items)
			}
		}
	}
}
