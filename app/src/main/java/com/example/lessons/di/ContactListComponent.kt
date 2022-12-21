package com.example.lessons.di

import com.example.lessons.contactlist.ContactListFragment
import dagger.Component


@ContactListScope
@Component(dependencies = [AppComponent::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}