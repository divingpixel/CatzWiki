package com.epikron.catzwiki.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject
import kotlin.reflect.KClass

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val viewModelClass: KClass<VM>,
    private val vbClazz: Class<VB>
) : DaggerFragment() {

    private var permissionPositiveAction: (() -> Unit)? = null
    private var permissionNegativeAction: (() -> Unit)? = null

    var onCloseListener: ((bundle: Bundle?) -> Unit)? = null
    private val fragmentDisposable: CompositeDisposable = CompositeDisposable()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    protected lateinit var binding: VB

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = vbClazz.getMethod("inflate", LayoutInflater::class.java)(null, this.layoutInflater) as VB
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass.java]
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) permissionPositiveAction?.invoke() else permissionNegativeAction?.invoke()
            }
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    fun checkPermission(
        permission: String,
        positiveAction: (() -> Unit)? = null,
        negativeAction: (() -> Unit)? = null,
        context: Context? = null
    ) {
        permissionPositiveAction = positiveAction
        permissionNegativeAction = negativeAction
        if (ActivityCompat.checkSelfPermission(context ?: requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            positiveAction?.invoke()
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                // here the android permission dialog is shown
            } else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    fun Disposable.observe() {
        fragmentDisposable.add(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!fragmentDisposable.isDisposed) fragmentDisposable.dispose()
    }
}
