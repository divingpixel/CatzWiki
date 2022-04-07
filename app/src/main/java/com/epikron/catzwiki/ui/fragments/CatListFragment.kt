package com.epikron.catzwiki.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.FragmentCatListBinding
import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.presentation.CatListViewModel
import com.epikron.catzwiki.ui.adapters.CatListViewAdapter
import com.epikron.catzwiki.ui.ListVerticalDecorator
import com.epikron.catzwiki.ui.popInfoDialog
import com.epikron.catzwiki.ui.views.CatButtonView
import com.epikron.catzwiki.ui.views.CatDetailView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlin.collections.ArrayList

class CatListFragment : BaseFragment<FragmentCatListBinding, CatListViewModel>
    (CatListViewModel::class, FragmentCatListBinding::class.java) {
    companion object {
        const val CAT_LIST_FRAGMENT_KEY = "cat_list_fragment_key"
    }

    private var cats: List<CatButtonView.CatButtonViewData> = listOf()
    private var countries: ArrayList<String> = arrayListOf()
    private val catListAdapter = CatListViewAdapter()
    private lateinit var countryListAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setListeners()
        observeDataChanges()
    }

    private fun setUpViews() {
        countryListAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        with(binding) {
            catList.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = catListAdapter
                if (itemDecorationCount > 1) removeItemDecorationAt(1)
                addItemDecoration(ListVerticalDecorator(R.dimen.double_thickness, 0, true), 0)
            }
            catCountry.adapter = countryListAdapter
        }
    }

    private fun observeDataChanges() {
        viewModel.allBreeds.subscribeBy(
            onSuccess = { catsList -> setCatsLists(catsList) },
            onError = { error -> showError(error) }
        ).observe()
        viewModel.displayCat.subscribe { showCat(it) }.observe()
    }

    private fun setCatsLists(catsList: List<BreedModel>) {
        binding.catProgress.isVisible = false
        cats = viewModel.getCats(catsList)
        catListAdapter.setData(cats)
        countries.apply {
            add("All origins")
            addAll(catsList.map { it.origin ?: "All origins" }.distinct().sortedBy { it })
        }
        countryListAdapter.clear()
        countryListAdapter.addAll(countries)
        countryListAdapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        binding.catCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                catListAdapter.setData(if (pos > 0) cats.filter { it.countryName == countries[pos] } else cats)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                catListAdapter.setData(cats)
            }
        }
        catListAdapter.onItemClickListener = { _, item -> item.name?.let { viewModel.setDisplayCat(it) } }
    }

    private fun showError(error: Throwable) {
        popInfoDialog(
            getString(R.string.network_error),
            error.message ?: getString(R.string.unknown_error),
            requireContext()
        )
    }

    private fun showCat(cat: BreedModel) {
        val customView = CatDetailView(requireContext()).apply { data = cat }
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogStyle)
            .setView(customView)
            .setNegativeButton(requireContext().getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
