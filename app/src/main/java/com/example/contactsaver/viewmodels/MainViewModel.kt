package com.example.contactsaver.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.contactsaver.models.Contact
import com.example.contactsaver.repository.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val contactRepository: ContactRepository):ViewModel() {

    val contactLists= liveData(Dispatchers.IO) {
        emit(contactRepository.getAllContact())
    }

    fun saveContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                contactRepository.insertContact(contact)
            }
        }

    }

}