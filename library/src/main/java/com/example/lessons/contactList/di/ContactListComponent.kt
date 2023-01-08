package com.example.lessons.contactList.di

import com.example.lessons.contactList.presentation.ContactListFragment
import dagger.Component

@ContactListScope
@Component(dependencies = [ContactListComponentDependencies::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}