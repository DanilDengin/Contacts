package com.example.lessons.di

import com.example.lessons.contactdetails.ContactDetailsFragment
import dagger.Component

@ContactDetailsScope
@Component(dependencies = [AppComponent::class],
modules = [ContactDetailsModule::class])
interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}