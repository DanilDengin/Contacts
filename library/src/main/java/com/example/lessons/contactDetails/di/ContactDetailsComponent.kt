package com.example.lessons.contactDetails.di

import com.example.lessons.contactDetails.presentation.ContactDetailsFragment
import com.example.lessons.di.ContactComponentDependencies
import dagger.Component


@ContactDetailsScope
@Component(
    dependencies = [ContactComponentDependencies::class],
    modules = [ContactDetailsModule::class]
)
internal interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}