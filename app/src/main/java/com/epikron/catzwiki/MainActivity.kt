package com.epikron.catzwiki

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.epikron.catzwiki.databinding.ActivityMainBinding
import com.epikron.catzwiki.presentation.MainViewModel
import com.epikron.catzwiki.ui.fragments.CatListFragment
import com.epikron.catzwiki.ui.popInfoDialog
import com.epikron.catzwiki.utils.Write
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory
	private lateinit var mainViewModel: MainViewModel

	private lateinit var binding: ActivityMainBinding

	private var backPressTime: Long = System.currentTimeMillis()
	private val disposable: CompositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		RxJavaPlugins.setErrorHandler(Write::consoleError)

		mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
		mainViewModel.registerConnectivityObserver(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

		onBackPressedDispatcher.addCallback(this, callback)

		disposable.add(
			mainViewModel.onlineStatus.subscribe { isOnline ->
				if (!isOnline) popInfoDialog(
					getString(R.string.no_internet),
					getString(R.string.no_internet_text),
					this
				)
			}
		)

		if (savedInstanceState == null) loadSplashFragment()
	}

	private fun loadSplashFragment() {
		if (supportFragmentManager.findFragmentByTag(CatListFragment.CAT_LIST_FRAGMENT_KEY) == null)
			supportFragmentManager.beginTransaction()
				.replace(
					binding.mainFragmentContainer.id,
					CatListFragment(),
					CatListFragment.CAT_LIST_FRAGMENT_KEY
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
				finishAndRemoveTask()
			}
		}
	}

	override fun onDestroy() {
		if (!disposable.isDisposed) disposable.dispose()
		super.onDestroy()
	}
}
