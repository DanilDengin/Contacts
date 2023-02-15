package com.example.lessons.test

import com.example.lessons.contactList.presentation.ContactListFragmentTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModuleTest::class])
internal interface AppComponentTest {

    fun inject(contactListFragmentTest: ContactListFragmentTest)

    @Component.Factory
    interface Factory {
        fun create(): AppComponentTest
    }
}