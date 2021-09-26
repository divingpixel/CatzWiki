package com.epikron.catzwiki.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(private val context : Context) {

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, value: String?): String {
        return context.getString(resId, value)
    }
}