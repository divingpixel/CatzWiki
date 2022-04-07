package com.epikron.catzwiki.remote

import javax.inject.Inject

class CatApiRepository @Inject constructor(
	private val catApiService: CatApiService
) {
	fun getAllBreeds() = catApiService.getAllBreeds()
}