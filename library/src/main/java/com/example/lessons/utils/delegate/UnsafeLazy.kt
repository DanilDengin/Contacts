package com.example.lessons.utils.delegate

internal fun <T> unsafeLazy(initializer: () -> T): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE, initializer = initializer)