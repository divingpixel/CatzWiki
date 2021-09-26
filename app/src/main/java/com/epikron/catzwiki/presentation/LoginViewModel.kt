package com.epikron.catzwiki.presentation

import androidx.lifecycle.ViewModel
import com.epikron.catzwiki.model.ApiResult
import com.epikron.catzwiki.model.SUCCESS
import com.epikron.catzwiki.model.UserModel
import com.epikron.catzwiki.model.WRONG_CREDENTIALS
import com.epikron.catzwiki.remote.UserRepository
import com.epikron.catzwiki.utils.BaseSchedulerProvider
import com.epikron.catzwiki.utils.SchedulerProvider
import com.epikron.catzwiki.utils.capitalizeFirstLetter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

enum class EmailValidation(val description: String) {
	GOOD("All good"),
	EMPTY("Email is empty"),
	SYNTAX("Format should be like account@domain.net"),
	DOMAIN("This domain is not accepted")
}

enum class PasswordValidation(val description: String) {
	GOOD("All good"),
	EMPTY("Password is empty"),
	SHORT("Password too short"),
	DIFFERENT("Passwords are different"),
	WEAK("Password too weak. Should be at least 8 characters long with one capital letter and one symbol")
}

@Singleton
class LoginViewModel @Inject constructor(
	private val schedulerProvider: BaseSchedulerProvider,
	private val mainViewModel: MainViewModel,
	private val userRepository: UserRepository
) : ViewModel() {

	private val disposable: CompositeDisposable = CompositeDisposable()

	fun getUser() = mainViewModel.user

	fun doLogin(email: String, password: String) {
		disposable.add(
			userRepository.getUser(email, password)
				.subscribeOn(schedulerProvider.io())
				.observeOn(schedulerProvider.ui())
				.doOnError { mainViewModel.user.onNext(UserModel(returnCode = -1)) }
				.subscribeBy(onSuccess = { mainViewModel.user.onNext(it) })
		)
	}

	fun isValidEmail(email: String, domainList: Array<String>? = null): EmailValidation {
		var result = EmailValidation.GOOD
		if (email.trim().isEmpty()) return EmailValidation.EMPTY
		val mailRegex = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}".toRegex()
		if (domainList != null && domainList.isNotEmpty()) {
			var inDomain = false
			for (domain in domainList) {
				val searchText = if (domain.contains(".")) "@$domain" else "@$domain."
				if (email.contains(searchText, true) && !inDomain) inDomain = true
			}
			if (!inDomain) result =
				EmailValidation.DOMAIN
		}
		if (!mailRegex.containsMatchIn(email.lowercase(Locale.ROOT))) result =
			EmailValidation.SYNTAX
		return result
	}

	fun isValidPassword(
		password: String,
		minLength: Int,
		checkStrength: Boolean = true
	): PasswordValidation {
		var result = PasswordValidation.GOOD
		if (password.isEmpty()) return PasswordValidation.EMPTY

		val strongRegex =
			"^.*(?=.{8,})(?=.*[!@#\$%^&*()\\-_=+{};:,<.>])(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*\$".toRegex()
		if (password.length < minLength)
			result = PasswordValidation.SHORT
		else if (!strongRegex.containsMatchIn(password) && checkStrength)
			result = PasswordValidation.WEAK
		return result
	}

	override fun onCleared() {
		super.onCleared()
		if (!disposable.isDisposed) disposable.dispose()
	}
}
