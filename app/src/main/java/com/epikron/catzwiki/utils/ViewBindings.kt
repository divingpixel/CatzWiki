@file:Suppress("unused")

package com.epikron.catzwiki.utils

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> Fragment.viewBindings(): Lazy<VB> =
    ViewBindingLazy(VB::class.java, this)

class ViewBindingLazy<out VB : ViewBinding>(
    private val clazz: Class<VB>,
    private val fragment: Fragment
) : Lazy<VB> {

    private var cachedValue: VB? = null

    private val inflateViewBindingMethod by lazy(LazyThreadSafetyMode.NONE) {
        clazz.getMethod("inflate", LayoutInflater::class.java)
    }

    override val value: VB
        get() {
            if (cachedValue == null) {
                @Suppress("UNCHECKED_CAST")
                cachedValue = inflateViewBindingMethod(null, fragment.layoutInflater) as VB
                fragment.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            cachedValue = null
                            fragment.viewLifecycleOwner.lifecycle.removeObserver(this)
                        }
                    }
                })
            }
            return cachedValue ?: throw IllegalStateException("View not found")
        }

    override fun isInitialized(): Boolean = cachedValue != null

}
