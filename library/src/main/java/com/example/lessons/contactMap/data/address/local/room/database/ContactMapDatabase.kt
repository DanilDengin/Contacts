package com.example.lessons.contactMap.data.address.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lessons.contactMap.data.address.local.room.model.ContactMapDao
import com.example.lessons.contactMap.data.address.local.room.model.ContactMapDbEntity

@Database(
    version = 1,
    entities = [ContactMapDbEntity::class],
    exportSchema = false
)
abstract class ContactMapDatabase : RoomDatabase() {

    internal abstract fun getContactMapDao(): ContactMapDao

}