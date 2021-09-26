package com.epikron.catzwiki.presentation

import androidx.lifecycle.ViewModel
import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.remote.CatApiRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatListViewModel @Inject constructor(
	private val mainViewModel: MainViewModel,
	private val catApiRepository: CatApiRepository
) : ViewModel() {

	val searchResult: BehaviorSubject<List<BreedModel>> = BehaviorSubject.create()
	private val disposable: CompositeDisposable = CompositeDisposable()

	fun getAllBreeds() = mainViewModel.allBreeds

	fun displayCat() = mainViewModel.selectedCat

	fun setDisplayCat(catName: String) {
		mainViewModel.allBreeds.value?.firstOrNull { it.name == catName }?.let {
			mainViewModel.selectedCat.onNext(it)
		}
	}

	// not necessary because this can be filtered from allBreeds, but just for the fun of it
	fun searchBreeds(query: String) {
		disposable.add(
			catApiRepository.searchBreeds(query)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeBy(
					onSuccess = { searchResult.onNext(it) },
					onError = { searchResult.onError(it) }
				)
		)
	}

	override fun onCleared() {
		super.onCleared()
		if (!disposable.isDisposed) disposable.dispose()
	}
}
