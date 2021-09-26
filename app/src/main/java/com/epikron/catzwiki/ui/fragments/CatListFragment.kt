package com.epikron.catzwiki.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.FragmentCatListBinding
import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.model.toCatButtonData
import com.epikron.catzwiki.presentation.CatListViewModel
import com.epikron.catzwiki.ui.views.CatButtonViewData
import com.epikron.catzwiki.ui.adapters.CatListViewAdapter
import com.epikron.catzwiki.ui.ListVerticalDecorator
import com.epikron.catzwiki.ui.popInfoDialog
import com.epikron.catzwiki.utils.*
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlin.collections.ArrayList

class CatListFragment : BaseFragment<CatListViewModel>(CatListViewModel::class) {
	companion object {
		const val CAT_LIST_FRAGMENT_KEY = "cat_list_fragment_key"
	}

	override val binding: FragmentCatListBinding by viewBindings()

	private var breeds: List<CatButtonViewData> = listOf()
	private var countries: ArrayList<String> = arrayListOf()
	private val catListAdapter = CatListViewAdapter()
	private lateinit var countryListAdapter: ArrayAdapter<String>

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setListeners()
		watchDataChanges()
	}

	private fun setUpViews() {
		countryListAdapter =
			ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
				.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
		with(binding) {
			catList.run {
				layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
				adapter = catListAdapter
				if (itemDecorationCount > 1) removeItemDecorationAt(1)
				addItemDecoration(ListVerticalDecorator(R.dimen.double_thickness), 0)
			}
			catCountry.adapter = countryListAdapter
		}
	}

	private fun watchDataChanges() {
		viewModel.getAllBreeds().subscribeBy(
			onSuccess = { breedList -> setCatsLists(breedList) },
			onError = { error -> showError(error) }
		).watch()
		viewModel.displayCat().subscribe {
			BottomFragment.popUp(BottomFragment.FragmentType.CAT_DETAILS, childFragmentManager)
		}.watch()
		viewModel.searchResult.subscribeBy(
			onNext = { breedList ->
				catListAdapter.setData(
					breedList.map { it.toCatButtonData() }.sortedBy { it.name }
				)
			},
			onError = { error ->
				showError(error)
				catListAdapter.setData(breeds)
			}
		).watch()
	}

	private fun showError(error: Throwable) {
		popInfoDialog(
			getString(R.string.network_error),
			error.message ?: getString(R.string.unknown_error),
			requireContext()
		)
	}

	private fun setCatsLists(breedList: List<BreedModel>) {
		breeds = breedList.map { it.toCatButtonData() }.sortedBy { it.name }
		countries.apply {
			add("All origins")
			addAll(breedList.map { it.origin ?: "All origins" }.distinct().sortedBy { it })
		}
		breedList.sortedBy { it.countryCode }
		catListAdapter.setData(breeds)
		countryListAdapter.clear()
		countryListAdapter.addAll(countries)
		countryListAdapter.notifyDataSetChanged()
	}

	private fun setListeners() {
		binding.catCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long) {
				catListAdapter.setData(
					if (pos > 0) breeds.filter { it.countryName == countries[pos] } else breeds
				)
			}

			override fun onNothingSelected(p0: AdapterView<*>?) {
				catListAdapter.setData(breeds)
			}
		}

		catListAdapter.onItemClickListener =
			{ _, item -> item.name?.let { viewModel.setDisplayCat(it) } }

		binding.catSearchInput.doAfterTextChanged {
			if (it?.isNotBlank() == true) {
				viewModel.searchBreeds(it.toString())
			} else {
				catListAdapter.setData(breeds)
			}
		}
	}
}
