package com.example.lessons.contactList.di

import com.example.lessons.contactList.presentation.ContactListFragment
import com.example.lessons.di.ContactComponentDependencies
import dagger.Component


@ContactListScope
@Component(
    dependencies = [ContactComponentDependencies::class],
    modules = [ContactListModule::class]
)
internal interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}