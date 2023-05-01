package com.example.utils.tag

fun Any.tagObj(): String = this::class.java.simpleName.takeIf { it.isNotEmpty() } ?: "TAG"
