package com.example.lessons.contactDetails.di

import com.example.lessons.contactDetails.presentation.ContactDetailsFragment
import com.example.lessons.di.AppComponent
import dagger.Component

@ContactDetailsScope
@Component(dependencies = [AppComponent::class])
interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}