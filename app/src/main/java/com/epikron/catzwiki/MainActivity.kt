package com.epikron.catzwiki

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.epikron.catzwiki.databinding.ActivityMainBinding
import com.epikron.catzwiki.presentation.MainViewModel
import com.epikron.catzwiki.ui.fragments.CatListFragment
import com.epikron.catzwiki.ui.fragments.LoginFragment
import com.epikron.catzwiki.ui.popInfoDialog
import com.epikron.catzwiki.utils.Write
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory
	private lateinit var mainVM: MainViewModel

	private lateinit var binding: ActivityMainBinding

	private var backPressTime: Long = System.currentTimeMillis()
	private val disposable: CompositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		RxJavaPlugins.setErrorHandler(Write::consoleError)

		mainVM = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

		mainVM.registerConnectivityObserver(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

		mainVM.getAllBreeds()

		if (savedInstanceState == null) loadLoginFragment()

		onBackPressedDispatcher.addCallback(this, callback)

		disposable.add(
			mainVM.onlineStatus.subscribe { isOnline ->
				if (!isOnline) popInfoDialog(
					getString(R.string.no_internet),
					getString(R.string.no_internet_text),
					this
				)
			}
		)
	}

	private fun loadLoginFragment() {
		if (supportFragmentManager.findFragmentByTag(LoginFragment.LOGIN_FRAGMENT_KEY) == null)
			supportFragmentManager.beginTransaction()
				.replace(
					binding.mainFragmentContainer.id,
					LoginFragment(),
					LoginFragment.LOGIN_FRAGMENT_KEY
				)
				.commitAllowingStateLoss()
	}


	private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			val oldBackPressTime = backPressTime
			backPressTime = System.currentTimeMillis()
			if (backPressTime - oldBackPressTime > 3000) {
				Toast.makeText(
					applicationContext,
					getString(R.string.back_to_close),
					Toast.LENGTH_SHORT
				).show()
			} else {
				moveTaskToBack(true)
			}
		}
	}

	override fun onDestroy() {
		if (!disposable.isDisposed) disposable.dispose()
		super.onDestroy()
	}
}
