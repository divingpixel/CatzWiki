package com.epikron.catzwiki.remote

import com.epikron.catzwiki.model.UserModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

	@FormUrlEncoded
	@POST("users/user")
	fun getUser(
		@Field("email") email: String,
		@Field("pass") password: String,
	): Single<UserModel>

}