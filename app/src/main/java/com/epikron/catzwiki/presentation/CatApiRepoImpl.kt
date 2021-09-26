package com.epikron.catzwiki.presentation

import com.epikron.catzwiki.model.ImageModel
import com.epikron.catzwiki.remote.CatApiService
import com.epikron.catzwiki.remote.CatApiRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CatApiRepoImpl @Inject constructor(
	private val catApiService: CatApiService
) : CatApiRepository {

	override fun getAllBreeds() = catApiService.getAllBreeds()

	override fun searchBreeds(query: String) = catApiService.searchBreeds(query)

	override fun searchImage(id: String): Single<ImageModel> = catApiService.getBreedImage(id)
}