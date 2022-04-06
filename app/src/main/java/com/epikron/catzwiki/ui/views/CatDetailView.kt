package com.epikron.catzwiki.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ScrollView
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.ViewCatDetailBinding
import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.utils.*

class CatDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    var data : BreedModel = BreedModel()
        set(value) {
            field = value
            setValues(value)
        }

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun setValues(cat : BreedModel) {
        val binding = ViewCatDetailBinding.inflate(LayoutInflater.from(context), this)
        with(binding) {
            detailsName.text = cat.name
            detailsImage.loadImage(cat.image?.url, R.drawable.ic_catz)
            detailsTemperament.text = cat.temperament
            detailsDescription.text = cat.description
            detailsCountry.text = context.getString(R.string.details_country, cat.origin)
            detailsFlag.loadImage(context.getString(R.string.flag_url, cat.countryCode?.lowercase() ?: ""), R.drawable.ic_paw)
            detailsAffectionRating.progress = cat.affectionLevel ?: 0
            detailsFriendlinessRating.progress = cat.strangerFriendly ?: 0
            detailsIntelligenceRating.progress = cat.intelligence ?: 0
            detailsEnergyRating.progress = cat.energyLevel ?: 0
            detailsSheddingRating.progress = cat.sheddingLevel ?: 0
            detailsLink.text = cat.wikipediaUrl
        }
    }
}
