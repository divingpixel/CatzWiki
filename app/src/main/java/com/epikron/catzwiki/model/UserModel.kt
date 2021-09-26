package com.epikron.catzwiki.model

data class UserModel(
	val id: String? = null,
	val email: String? = null,
	val name: String? = null,
	val avatar: String? = null,
	val apiToken: String? = null,
	val returnCode: Int? = null
)

const val SUCCESS = 0
const val UNKNOWN = -1
const val WRONG_CREDENTIALS = 101
