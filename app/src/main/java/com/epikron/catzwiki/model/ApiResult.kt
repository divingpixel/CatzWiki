package com.epikron.catzwiki.model

data class ApiResult<T : Any>(
	val result: T? = null,
	val error: Throwable? = null
)