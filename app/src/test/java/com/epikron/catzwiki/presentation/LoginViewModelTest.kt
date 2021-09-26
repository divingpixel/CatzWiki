package com.epikron.catzwiki.presentation

import androidx.lifecycle.ViewModel
import com.epikron.catzwiki.model.SUCCESS
import com.epikron.catzwiki.model.UserModel
import com.epikron.catzwiki.remote.UserRepository
import com.epikron.catzwiki.utils.SchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : ViewModel() {

	@MockK(relaxed = true)
	private lateinit var schedulersProvider: SchedulerProvider

	@MockK(relaxed = true)
	private lateinit var userRepository: UserRepository

	@MockK(relaxed = true)
	private lateinit var viewModel: LoginViewModel

	@MockK(relaxed = true)
	private lateinit var mainViewModel: MainViewModel

	private val disposable: CompositeDisposable = CompositeDisposable()

	private val mockUser = UserModel(
		id = "1234",
		email = "john.doe@mail.com",
		name = "John Doe",
		returnCode = SUCCESS
	)

	@Before
	fun setUp() {
		MockKAnnotations.init(this)
		viewModel = LoginViewModel(schedulersProvider, mainViewModel, userRepository)
	}

	@After
	fun tearDown() {
		disposable.dispose()
		unmockkAll()
	}

	@Test
	fun `when the user is requested the correct result is provided`() {
		val result: BehaviorSubject<UserModel> = BehaviorSubject.create()
		result.onNext(mockUser)

		every { viewModel.getUser() } returns result

		viewModel.getUser()

		assert(mainViewModel.user.value == mockUser)
	}

	@Test
	fun `when the login method is called the user is provided`() {
		every { userRepository.getUser(any(), any()) } returns Single.just(mockUser)

		viewModel.doLogin("x@x.com", "x")

		verify { userRepository.getUser(any(),any()) }
	}

	@Test
	fun `when a valid mail is given the result is valid`() {
		val result = viewModel.isValidEmail("mail@test.com", arrayOf("test.com"))
		assertEquals(EmailValidation.GOOD, result)
	}

	@Test
	fun `when a valid mail but with wrong domain is given the result is invalid`() {
		val result = viewModel.isValidEmail("mail@mail.com", arrayOf("test.com"))
		assertEquals(EmailValidation.DOMAIN, result)
	}

	@Test
	fun `when an invalid mail is given the result is invalid`() {
		val result = viewModel.isValidEmail("mail@test", arrayOf("test.com"))
		assertEquals(EmailValidation.SYNTAX, result)
	}

	@Test
	fun `when a strong password is given the result is valid`() {
		val password = "Asdfgh!1"
		val minLength = 8
		val result = viewModel.isValidPassword(password, minLength)
		assertEquals(PasswordValidation.GOOD, result)
	}

	@Test
	fun `when a weak password is given the result is invalid`() {
		val password = "asdfghi1"
		val minLength = 8
		val result = viewModel.isValidPassword(password, minLength)
		assertEquals(PasswordValidation.WEAK, result)
	}

	@Test
	fun `when a short password is given the result is invalid`() {
		val password = "asdfgh"
		val minLength = 8
		val result = viewModel.isValidPassword(password, minLength)
		assertEquals(PasswordValidation.SHORT, result)
	}
}
