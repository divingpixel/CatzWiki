package com.epikron.catzwiki.presentation

import androidx.lifecycle.ViewModel
import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.ui.views.CatButtonView.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.SingleSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatListViewModel @Inject constructor(
	private val mainViewModel: MainViewModel
) : ViewModel() {

	val allBreeds: SingleSubject<List<BreedModel>> = mainViewModel.allBreeds
	val displayCat = mainViewModel.selectedCat
	private val disposable: CompositeDisposable = CompositeDisposable()

	init {
		mainViewModel.getAllBreeds()
	}

	fun setDisplayCat(catName: String) {
		allBreeds.value?.firstOrNull { it.name == catName }?.let {
			mainViewModel.selectedCat.onNext(it)
		}
	}

	fun getCats(breedList: List<BreedModel>) : List<CatButtonViewData> =
		breedList.map { it.toCatButtonData() }.sortedBy { it.name }

	override fun onCleared() {
		super.onCleared()
		if (!disposable.isDisposed) disposable.dispose()
	}
}
