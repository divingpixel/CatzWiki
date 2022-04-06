package com.epikron.catzwiki.ui

import android.content.Context
import com.epikron.catzwiki.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun popInfoDialog(title: String, text: String, context: Context, action: (() -> Unit)? = null) {
    MaterialAlertDialogBuilder(context, R.style.CustomDialogStyle)
        .setTitle(title)
        .setMessage(text)
        .setPositiveButton(context.getString(android.R.string.ok)) { dialog, _ ->
            action?.invoke()
            dialog.dismiss()
        }
        .show()
}
