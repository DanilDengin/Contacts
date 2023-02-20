package com.example.db.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.db.entity.ContactMapDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createContactAddress(contactMapDbEntity: ContactMapDbEntity)

    @Query("SELECT * FROM contacts")
    fun getAllAddresses(): Flow<List<ContactMapDbEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getAddressById(id: String):ContactMapDbEntity?

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactAddress(id: String)
}