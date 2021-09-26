package com.epikron.catzwiki.ui

import android.app.Dialog
import android.content.Context
import android.view.View
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

fun popConfirmationDialog(
    title: String,
    text: String,
    context: Context,
    positiveAction: (() -> Unit),
    negativeAction: (() -> Unit)? = null,
    positiveButtonRes: Int = android.R.string.ok,
    negativeButtonRes: Int = android.R.string.cancel
) {
    MaterialAlertDialogBuilder(context, R.style.CustomDialogStyle)
        .setCancelable(false)
        .setTitle(title)
        .setMessage(text)
        .setNegativeButton(context.getString(negativeButtonRes)) { dialog, _ ->
            negativeAction?.invoke()
            dialog.dismiss()
        }
        .setPositiveButton(context.getString(positiveButtonRes)) { dialog, _ ->
            positiveAction.invoke()
            dialog.dismiss()
        }
        .show()
}

fun popCustomDialog(
    title: String,
    customView: View,
    context: Context,
    positiveAction: (() -> Unit)? = null,
    neutralAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null,
    positiveButtonRes: Int? = null,
    neutralButtonRes: Int? = null,
    negativeButtonRes: Int? = null
) : Dialog {
    val dialog = MaterialAlertDialogBuilder(context, R.style.CustomDialogStyle)
        .setTitle(title)
        .setCancelable(false)
        .setView(customView)
    negativeButtonRes?.let {
        dialog.setNegativeButton(context.getString(it)) { dialog, _ ->
            negativeAction?.invoke()
            dialog.dismiss()
        }
    }
    positiveButtonRes?.let {
        dialog.setPositiveButton(context.getString(it)) { dialog, _ ->
            positiveAction?.invoke()
            dialog.dismiss()
        }
    }
    neutralButtonRes?.let {
        dialog.setNeutralButton(context.getString(it)) { dialog, _ ->
            neutralAction?.invoke()
            dialog.dismiss()
        }
    }
    return dialog.show()
}
