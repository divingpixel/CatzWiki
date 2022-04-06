package com.epikron.catzwiki.utils

import android.widget.ImageView
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
