package com.example.lessons.contactList.di

import com.example.lessons.contactList.presentation.ContactListFragment
import com.example.lessons.di.AppComponent
import dagger.Component


@ContactListScope
@Component(dependencies = [AppComponent::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}