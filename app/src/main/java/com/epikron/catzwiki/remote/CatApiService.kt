package com.epikron.catzwiki.remote

import com.epikron.catzwiki.model.BreedModel
import com.epikron.catzwiki.model.ImageModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {

	@GET("breeds")
	fun getAllBreeds(): Single<List<BreedModel>>

	@GET("breeds/search")
	fun searchBreeds(@Query("q") breed: String): Single<List<BreedModel>>

	@GET("images/{id}")
	fun getBreedImage(@Path("id") id: String): Single<ImageModel>
}