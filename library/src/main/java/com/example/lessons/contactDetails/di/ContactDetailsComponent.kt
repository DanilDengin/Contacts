package com.example.lessons.contactDetails.di

import com.example.lessons.contactDetails.presentation.ContactDetailsFragment
import dagger.Component

@ContactDetailsScope
@Component(dependencies = [ContactDetailsComponentDependencies::class])
interface ContactDetailsComponent  {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}