package com.example.mvvm

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

inline fun <reified VM : ViewModel> Fragment.getComponentViewModel(): VM {
    return ViewModelProvider(this).get()
}