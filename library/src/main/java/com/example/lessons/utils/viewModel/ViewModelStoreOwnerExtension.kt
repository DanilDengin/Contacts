package com.example.lessons.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

internal inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(crossinline initializer: () -> T): T {
    return ViewModelProvider(this, vmFactory(initializer)).get(T::class.java)
}

internal inline fun <VM : ViewModel> vmFactory(crossinline initializer: () -> VM) =
    object : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = initializer() as T
    }