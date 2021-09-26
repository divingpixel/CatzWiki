package com.epikron.catzwiki.model

data class SimpleImageModel(
	val height: Int? = null,
	val id: String? = null,
	val url: String? = null,
	val width: Int? = null
)

data class ImageModel(
	val height: Int? = null,
	val id: String? = null,
	val url: String? = null,
	val width: Int? = null,
	val breeds: List<BreedModel>? = null
)