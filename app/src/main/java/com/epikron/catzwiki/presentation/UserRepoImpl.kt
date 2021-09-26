package com.epikron.catzwiki.presentation

import com.epikron.catzwiki.model.SUCCESS
import com.epikron.catzwiki.model.UNKNOWN
import com.epikron.catzwiki.model.UserModel
import com.epikron.catzwiki.model.WRONG_CREDENTIALS
import com.epikron.catzwiki.remote.UserRepository
import com.epikron.catzwiki.remote.UserService
import com.epikron.catzwiki.utils.capitalizeFirstLetter
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
	private val userService: UserService
) : UserRepository {

	override fun getUser(email: String, password: String): Single<UserModel> {

		return Single.just(
			UserModel(
				id = UUID.randomUUID().toString(),
				email = email,
				name = email.split("@").first().split(".", "_")
					.joinToString(" ") { it.capitalizeFirstLetter() },
				returnCode = listOf(SUCCESS, UNKNOWN, SUCCESS, WRONG_CREDENTIALS, SUCCESS).random()
			)
		)

		//return userService.getUser(email, password)
	}
}
