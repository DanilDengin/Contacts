package com.example.lessons.contactList.di

import com.example.lessons.contactList.presentation.ContactListFragment
import com.example.lessons.contactMap.di.ContactMapComponent
import com.example.lessons.di.contactListDetails.ContactComponentDependencies
import com.example.lessons.di.contactMap.MapComponentDependencies
import dagger.Component

@ContactListScope
@Component(
    dependencies = [ContactComponentDependencies::class],
    modules = [ContactListModule::class]
)
internal interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)

    @Component.Factory
    interface Factory {
        fun create(contactComponentDependencies: ContactComponentDependencies): ContactListComponent
    }
}