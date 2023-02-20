package com.example.utils.sharedPref

import android.content.SharedPreferences

fun SharedPreferences.putData(block: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.block()
    editor.apply()
}