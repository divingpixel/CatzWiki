package com.epikron.catzwiki.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.epikron.catzwiki.databinding.ViewPriceLineBinding

data class PriceLineViewData(
	val title: String? = null,
	val details: String? = null,
	val value: String? = null
)

class PriceLineView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

	private val binding = ViewPriceLineBinding.inflate(LayoutInflater.from(context),this)

	var state = PriceLineViewData()
		set(value) {
			field = value
			setValues(value)
		}

	init {
		layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	}

	private fun setValues(values: PriceLineViewData?) {
		with(binding) {
			priceLineTitle.text = values?.title ?: ""
			priceLineDetails.text = values?.details ?: run { priceLineDetails.isVisible = false; "" }
			priceLineValue.text = values?.value ?: ""
		}
	}
}
