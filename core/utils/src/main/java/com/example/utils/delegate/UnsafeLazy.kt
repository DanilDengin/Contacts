package com.example.utils.delegate

fun <T> unsafeLazy(initializer: () -> T): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE, initializer = initializer)