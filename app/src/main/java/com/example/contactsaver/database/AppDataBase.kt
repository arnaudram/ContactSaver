package com.example.contactsaver.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactsaver.models.Contact

@Database(entities = [Contact::class],version = 1,exportSchema = false)
abstract class AppDataBase:RoomDatabase() {
    abstract fun getContactDao():ContactDao

    companion object{
        private lateinit var singleInstance:AppDataBase

        fun getDataBase(application: Application): AppDataBase {
            if (!this::singleInstance.isInitialized){
                 synchronized(this){
                     singleInstance=Room.databaseBuilder(application.applicationContext,AppDataBase::class.java,"appdatabase.db")
                         .build()
                 }
            }
            return singleInstance
        }
    }
}