package com.example.contactsaver.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactsaver.repository.ContactRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val application: Application,private val contactRepository: ContactRepository) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(contactRepository) as T
        else
       throw IllegalArgumentException("Wrong view model class")
    }
}