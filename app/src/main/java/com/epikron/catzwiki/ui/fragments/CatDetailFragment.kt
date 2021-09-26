package com.epikron.catzwiki.ui.fragments

import android.os.Bundle
import android.view.View
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.FragmentCatDetailBinding
import com.epikron.catzwiki.presentation.CatDetailViewModel
import com.epikron.catzwiki.utils.*

class CatDetailFragment : BaseFragment<CatDetailViewModel>(CatDetailViewModel::class) {
	companion object {
		fun newInstance(bundle: Bundle?) = CatDetailFragment().apply { arguments = bundle }
	}

	override val binding: FragmentCatDetailBinding by viewBindings()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setViews()
	}

	private fun setViews() {
		viewModel.displayCat()?.let { cat ->
			with(binding) {
				detailsName.text = cat.name
				detailsImage.loadImage(cat.image?.url, R.drawable.ic_catz)
				detailsTemperament.text = cat.temperament
				detailsDescription.text = cat.description
				detailsCountry.text = getString(R.string.details_country, cat.origin)
				detailsFlag.loadImage(getString(R.string.flag_url, cat.countryCode?.lowercase() ?: ""), R.drawable.ic_paw)
				detailsAffectionRating.progress = cat.affectionLevel ?: 0
				detailsFriendlinessRating.progress = cat.strangerFriendly ?: 0
				detailsIntelligenceRating.progress = cat.intelligence ?: 0
				detailsEnergyRating.progress = cat.energyLevel ?: 0
				detailsSheddingRating.progress = cat.sheddingLevel ?: 0
				detailsLink.text = cat.wikipediaUrl
			}
		} ?: onCloseListener?.invoke(null)
	}
}
