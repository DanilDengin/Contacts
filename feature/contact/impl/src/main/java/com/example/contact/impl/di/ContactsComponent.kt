package com.example.contact.impl.di

import com.example.contact.impl.presentation.details.ContactDetailsFragment
import com.example.contact.impl.presentation.list.ContactListFragment
import com.example.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [ContactsModule::class],
    dependencies = [ContactsExternalDependencies::class]
)
internal interface ContactsComponent {

    @Component.Factory
    interface Factory {
        fun create(contactsExternalDependencies: ContactsExternalDependencies): ContactsComponent
    }

    fun inject(contactListFragment: ContactListFragment)

    fun inject(contactDetailsFragment: ContactDetailsFragment)
}