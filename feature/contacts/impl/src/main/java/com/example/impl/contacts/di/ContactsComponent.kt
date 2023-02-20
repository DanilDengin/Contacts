package com.example.impl.contacts.di

import com.example.di.FeatureScope
import com.example.impl.contacts.presentation.details.ContactDetailsFragment
import com.example.impl.contacts.presentation.list.ContactListFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [ContactsModule::class],
    dependencies = [ContactsExternalDependencies::class]
)
interface ContactsComponent {

    @Component.Factory
    interface Factory {
        fun create(contactsExternalDependencies: ContactsExternalDependencies): ContactsComponent
    }

    fun inject(contactListFragment: ContactListFragment)

    fun inject(contactDetailsFragment: ContactDetailsFragment)
}