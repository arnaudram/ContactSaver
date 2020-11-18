package com.example.contactsaver.repository

import androidx.lifecycle.LiveData
import com.example.contactsaver.database.ContactDao
import com.example.contactsaver.models.Contact

class ContactRepository(private val contactDao: ContactDao) {

    suspend fun insertContact(contact: Contact){
          contactDao.insertContact(contact)
    }
    suspend fun getAllContact(): List<Contact> {
        return contactDao.getAllContact()
    }

}