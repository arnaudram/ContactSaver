package com.example.contactsaver.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contactsaver.models.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
 interface ContactDaoFlow {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(vararg contact:Contact)

    @Query("SELECT * FROM contact_table ORDER BY name ASC ")
    fun getAllContact():Flow<List<Contact>>



}