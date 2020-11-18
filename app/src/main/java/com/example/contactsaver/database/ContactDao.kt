package com.example.contactsaver.database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.contactsaver.models.Contact
import kotlinx.coroutines.Deferred


@Dao
interface ContactDao {
    @Insert(onConflict =OnConflictStrategy.REPLACE)
   suspend fun insertContact(vararg  contacts:Contact)

    @Query("SELECT * FROM contact_table")
    suspend fun getAllContact():List<Contact>
    @Query("SELECT * FROM contact_table WHERE name LIKE:name")
   suspend fun findByName(name:String):List<Contact>
    @Delete
     suspend fun deleteContact(contact: Contact)

}