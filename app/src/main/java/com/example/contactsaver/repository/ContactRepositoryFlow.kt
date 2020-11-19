package com.example.contactsaver.repository

import androidx.lifecycle.LiveData
import com.example.contactsaver.database.ContactDaoFlow
import com.example.contactsaver.models.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class ContactRepositoryFlow(private  val contactDaoFlow: ContactDaoFlow) {

    fun getAllContact(): Flow<List<Contact>> {
      return  contactDaoFlow.getAllContact().distinctUntilChanged()
    }
   suspend fun insertContact( contact: Contact){
        contactDaoFlow.insertContact(contact)
    }
}