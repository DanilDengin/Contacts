package com.example.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.entity.ContactMapDbEntity
import com.example.db.model.ContactMapDao

@Database(
    version = 1,
    entities = [ContactMapDbEntity::class],
    exportSchema = false
)
abstract class ContactMapDatabase : RoomDatabase() {

    abstract fun getContactMapDao(): ContactMapDao

}