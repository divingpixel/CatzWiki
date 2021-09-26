package com.epikron.catzwiki.presentation

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatDetailViewModel @Inject constructor(
	private val mainViewModel: MainViewModel,
) : ViewModel() {

	fun displayCat() = mainViewModel.selectedCat.value ?: null
}
