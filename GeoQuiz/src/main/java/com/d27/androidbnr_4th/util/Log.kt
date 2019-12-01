package com.d27.androidbnr_4th.util

import android.util.Log
import com.d27.androidbnr_4th.BuildConfig

object Log {
    fun v(msg: String) {
        if(!BuildConfig.DEBUG) return
        Log.v(tag(), msg)
    }

    fun d(msg: String) {
        if(!BuildConfig.DEBUG) return
        Log.d(tag(), msg)
    }

    fun i(msg: String) {
        if(!BuildConfig.DEBUG) return
        Log.i(tag(), msg)
    }

    fun w(msg: String) {
        if(!BuildConfig.DEBUG) return
        Log.w(tag(), msg)
    }

    fun e(msg: String) {
        if(!BuildConfig.DEBUG) return
        Log.e(tag(), msg)
    }

    private fun tag(): String {
        val level = 4
        val trace = Thread.currentThread().stackTrace[level]
        val fileName = trace.fileName
        val classPath = trace.className
        val className = classPath.substring(classPath.lastIndexOf(".") + 1)
        val methodName = trace.methodName
        val lineNumber = trace.lineNumber
        return "****GeoQuiz****[$className] $methodName ($fileName:$lineNumber) "
    }
}