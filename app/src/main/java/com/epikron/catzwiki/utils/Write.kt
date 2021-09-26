package com.epikron.catzwiki.utils

import android.util.Log
import com.epikron.catzwiki.BuildConfig

@Suppress("unused")
object Write {
    private fun customLog(message: String, type: Char) {
        var callerClassName = Exception().stackTrace[3].className
        val stack = callerClassName.split("\\.".toRegex()).toTypedArray()
        if (stack.isNotEmpty()) callerClassName = stack[stack.size - 1]
        var tag = Exception().stackTrace[2].methodName
        tag = "$callerClassName -> $tag"
        if (BuildConfig.DEBUG) when (type) {
            'd' -> Log.d(tag, message)
            'i' -> Log.i(tag, message)
            'v' -> Log.v(tag, message)
            'w' -> Log.w(tag, message)
            'e' -> Log.e(tag, message)
        }
    }

    operator fun invoke(message : String) {
        customLog(message, 'i')
    }

    fun consoleDebug(what: String) {
        customLog(what, 'd')
    }

    fun consoleInfo(what: String) {
        customLog(what, 'i')
    }

    fun consoleMessage(what: String) {
        customLog(what, 'v')
    }

    fun consoleWarning(what: String) {
        customLog(what, 'w')
    }

    fun consoleError(what: String) {
        customLog(what, 'e')
    }

    fun consoleError(exception: java.lang.Exception) {
        customLog(exception.message.toString(), 'e')
    }

    fun consoleError(throwable: Throwable) {
        customLog(throwable.message.toString(), 'e')
    }

    fun printMethodStackTrace(methodName: String) {
        val invokers = Thread.currentThread().stackTrace
        val sb = StringBuilder("printMethodStackTrace ")
        sb.append(methodName)
        sb.append(" ")
        for (i in invokers.size - 1 downTo 4) {
            val invoker = invokers[i]
            sb.append(
                String.format(
                    "%s(%d).%s",
                    invoker.fileName,
                    invoker.lineNumber,
                    invoker.methodName
                )
            )
            if (i > 4) {
                sb.append("-->")
            }
        }
        Log.i("printMethodStackTrace", sb.toString())
    }
}
