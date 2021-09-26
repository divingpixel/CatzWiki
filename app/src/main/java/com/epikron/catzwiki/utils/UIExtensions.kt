package com.epikron.catzwiki.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?, errorResId: Int) {
	if(!isInEditMode) url?.let {
       Glide.with(this)
           .load(url)
           .placeholder(errorResId)
           .error(errorResId)
           .into(this)
    } ?: this.setImageResource(errorResId)
	else this.setImageResource(errorResId)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
