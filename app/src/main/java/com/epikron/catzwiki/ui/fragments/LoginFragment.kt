package com.epikron.catzwiki.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.epikron.catzwiki.R
import com.epikron.catzwiki.databinding.FragmentLoginBinding
import com.epikron.catzwiki.model.SUCCESS
import com.epikron.catzwiki.model.WRONG_CREDENTIALS
import com.epikron.catzwiki.presentation.EmailValidation
import com.epikron.catzwiki.presentation.LoginViewModel
import com.epikron.catzwiki.presentation.PasswordValidation
import com.epikron.catzwiki.ui.popInfoDialog
import com.epikron.catzwiki.utils.*

class LoginFragment : BaseFragment<LoginViewModel>(LoginViewModel::class) {
	companion object {
		const val LOGIN_FRAGMENT_KEY = "login_fragment_key"
	}

	override val binding: FragmentLoginBinding by viewBindings()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListeners()
		watchDataChanges()
		checkCredentials()
	}

	private fun setListeners() {
		with(binding) {
			loginEmailInput.doOnTextChanged { _, _, _, _ -> checkCredentials() }
			loginPasswordInput.doOnTextChanged { _, _, _, _ -> checkCredentials() }
			loginButton.setOnClickListener { processLogin() }
		}
	}

	private fun watchDataChanges() {
		viewModel.getUser().subscribe {
			if (it.returnCode == SUCCESS) {
				goToCatList()
			} else {
				showError(it.returnCode)
			}
		}.watch()
	}

	private fun processLogin() {
		with(binding) {
			if (checkCredentials()) {
				loginProgress.visibility = View.VISIBLE
				loginButton.requestFocus()
				loginButton.isEnabled = false
				hideKeyboard()
				viewModel.doLogin(
					loginEmailInput.text.toString(),
					loginPasswordInput.text.toString()
				)
			}
		}
	}

	private fun checkCredentials(): Boolean {
		var areCredentialsGood: Boolean
		with(binding) {
			val emailValidation = viewModel.isValidEmail(loginEmailInput.text.toString())
			val passValidation =
				viewModel.isValidPassword(loginPasswordInput.text.toString(), 8, false)
			loginEmailLayout.error =
				if (emailValidation != EmailValidation.GOOD) emailValidation.description else null
			loginPasswordLayout.error =
				if (passValidation != PasswordValidation.GOOD) passValidation.description else null
			areCredentialsGood =
				emailValidation == EmailValidation.GOOD && passValidation == PasswordValidation.GOOD
			loginButton.isEnabled = areCredentialsGood
		}
		return areCredentialsGood
	}

	private fun goToCatList() {
		parentFragmentManager
			.beginTransaction()
			.replace(
				R.id.main_fragment_container,
				CatListFragment(),
				CatListFragment.CAT_LIST_FRAGMENT_KEY
			)
			.commit()
	}

	private fun showError(code: Int?) {
		val error = when (code) {
			WRONG_CREDENTIALS -> getString(R.string.invalid_credentials)
			else -> null
		}
		binding.loginProgress.visibility = View.INVISIBLE
		popInfoDialog(
			getString(R.string.login_error),
			error ?: getString(R.string.unknown_error),
			requireContext()
		)
	}
}
