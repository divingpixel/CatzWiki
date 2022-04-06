package com.epikron.catzwiki.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.ViewCatButtonBinding
import com.epikron.catzwiki.utils.loadImage

class CatButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private lateinit var binding: ViewCatButtonBinding

    var data = CatButtonViewData()
        set(value) {
            field = value
            setValues(value)
        }

    var onClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            value?.let { action ->
                binding.catButton.setOnClickListener { action.invoke() }
            } ?: binding.catButton.setOnClickListener(null)
        }

    init {
        layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setValues(values: CatButtonViewData?) {
        if (!this::binding.isInitialized) binding = ViewCatButtonBinding.inflate(LayoutInflater.from(context), this)
        with(binding) {
            catButtonName.text = values?.name ?: ""
            val flagUrl = context.getString(R.string.flag_url, values?.countryCode?.lowercase() ?: "")
            catButtonFlag.loadImage(flagUrl, R.drawable.ic_paw)
            catButtonImage.loadImage(values?.imageRes, R.drawable.ic_no_image)
            catButtonTemperament.text = values?.temperament ?: ""
            catButtonDescription.text = values?.description
        }
    }

    data class CatButtonViewData(
        val name: String? = null,
        val description: String? = null,
        val countryCode: String? = null,
        val countryName: String? = null,
        val temperament: String? = null,
        val imageRes: String? = null
    )
}