package com.epikron.catzwiki.remote

import com.epikron.catzwiki.model.BreedModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CatApiService {

	@GET("breeds")
	fun getAllBreeds(): Single<List<BreedModel>>
}