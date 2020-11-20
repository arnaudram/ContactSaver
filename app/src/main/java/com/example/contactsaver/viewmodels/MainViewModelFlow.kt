package com.example.contactsaver.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.work.WorkManager
import com.example.contactsaver.models.Contact
import com.example.contactsaver.repository.ContactRepositoryFlow
import kotlinx.coroutines.*


class MainViewModelFlow(private val repositoryFlow: ContactRepositoryFlow):ViewModel() {
    private val scope=CoroutineScope(Dispatchers.Main + Job())



    fun saveContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repositoryFlow.insertContact(contact)
            }
        }
    }



    val contactLists= liveData {
        scope.launch {
            withContext(Dispatchers.IO) {
                val result = repositoryFlow.getAllContact().asLiveData()
                emitSource(result)
            }
        }.join()




        }



}





class MainViewModelFactoryFlow(private val application: Application, private val contactRepositoryFlow: ContactRepositoryFlow) :
        ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModelFlow::class.java))
            return MainViewModelFlow(contactRepositoryFlow) as T
        else
            throw IllegalArgumentException("Wrong view model class")
    }
}