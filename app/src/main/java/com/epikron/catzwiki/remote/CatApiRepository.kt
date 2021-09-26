package com.epikron.catzwiki.remote

import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.model.ImageModel
import io.reactivex.rxjava3.core.Single

interface CatApiRepository {

	fun getAllBreeds(): Single<List<BreedModel>>

	fun searchBreeds(query : String): Single<List<BreedModel>>

	fun searchImage(id : String): Single<ImageModel>
}