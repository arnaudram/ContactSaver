package com.example.contactsaver.worker

import android.app.Application

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.contactsaver.WorkerUtils
import com.example.contactsaver.database.AppDataBase
import com.example.contactsaver.models.Contact
import timber.log.Timber

class CreateContactWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    val application= Application()
    private val contactDao=AppDataBase.getDataBase(application).getContactDao()
    override suspend fun doWork(): Result {
        val name= inputData.getString("name")
        val email=inputData.getString("email")

        return try {
            if (name !=null && email !=null){
                val contact=Contact(name=name,email = email)
                contactDao.insertContact(contact)
                val message=" A newly contact name :${contact.name} has been added"
                Timber.i("new contact name : ${contact.name}")
               WorkerUtils.makeNotification(applicationContext,message )
            }
            Result.success()
        }catch (e:Throwable){
            Timber.e(e.message)
            Result.failure()

        }

    }
}