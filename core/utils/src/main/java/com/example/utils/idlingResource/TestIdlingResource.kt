package com.example.utils.idlingResource

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import java.util.concurrent.atomic.AtomicBoolean

class TestIdlingResource : IdlingResource {

    @Volatile
    private var mCallback: ResourceCallback? = null

    private var mIsIdleNow = AtomicBoolean(true)

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback?) {
        mCallback = callback
    }

    override fun isIdleNow(): Boolean {
        return mIsIdleNow.get()
    }

    fun setIdleState(isIdleNow: Boolean) {
        mIsIdleNow.set(isIdleNow)
        if (isIdleNow && mCallback != null) {
            requireNotNull(mCallback).onTransitionToIdle()
        }
    }
}