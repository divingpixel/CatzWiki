package com.epikron.catzwiki.remote

import com.epikron.catzwiki.model.UserModel
import io.reactivex.rxjava3.core.Single

interface UserRepository {

	fun getUser(email: String, password: String): Single<UserModel>

}
