package com.example.lessons.contactDetails.di

import com.example.lessons.contactDetails.presentation.ContactDetailsFragment
import com.example.lessons.contactMap.di.ContactMapComponent
import com.example.lessons.di.contactListDetails.ContactComponentDependencies
import com.example.lessons.di.contactMap.MapComponentDependencies
import dagger.Component

@ContactDetailsScope
@Component(
    dependencies = [ContactComponentDependencies::class],
    modules = [ContactDetailsModule::class]
)
internal interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(contactComponentDependencies: ContactComponentDependencies): ContactDetailsComponent
    }
}