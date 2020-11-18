package com.example.contactsaver.repository

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.contactsaver.TestUtil
import com.example.contactsaver.database.AppDataBase
import com.example.contactsaver.database.ContactDao
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.any
import org.mockito.Mockito.spy
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.O,Build.VERSION_CODES.LOLLIPOP))
class ContactRepositoryTest{
    private lateinit var contactDao: ContactDao
    private lateinit var appDataBase: AppDataBase
    private lateinit var repository:ContactRepository

    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()

    val testCoroutineDispatcher= TestCoroutineDispatcher()
    @Before
    fun setUp(){
        Dispatchers.setMain(testCoroutineDispatcher)
        val context= ApplicationProvider.getApplicationContext<Context>()
        appDataBase=Room.inMemoryDatabaseBuilder(context,AppDataBase::class.java).build()
        contactDao=appDataBase.getContactDao()
        repository= ContactRepository(contactDao)
    }
    @Test
    fun should_invoke_insertContact_from_dao(){
        val contact=TestUtil.createContact()
        CoroutineScope(testCoroutineDispatcher).launch {
            withContext(Dispatchers.IO){
                repository.insertContact(contact)
                verify(contactDao).insertContact(any())
            }
        }

    }
    @After
    @Throws(IOException::class)
    fun closeDb(){
        appDataBase.close()
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()

    }
}