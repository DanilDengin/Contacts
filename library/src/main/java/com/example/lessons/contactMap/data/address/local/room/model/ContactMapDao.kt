package com.example.lessons.contactMap.data.address.local.room.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ContactMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createContactAddress(contactMapDbEntity: ContactMapDbEntity)

    @Query("SELECT * FROM contacts")
    fun getAllAddresses(): Flow<List<ContactMapDbEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getAddressById(id: String): ContactMapDbEntity?

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactAddress(id: String)
}