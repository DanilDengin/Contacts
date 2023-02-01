package com.example.lessons.contactMap.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ContactMapArguments(val name: String, val id: String) : Parcelable