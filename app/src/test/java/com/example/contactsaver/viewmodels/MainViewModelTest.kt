package com.example.contactsaver.viewmodels

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.contactsaver.TestUtil
import com.example.contactsaver.database.AppDataBase
import com.example.contactsaver.database.ContactDao
import com.example.contactsaver.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoFramework
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    private val testScope = TestCoroutineScope()
    private lateinit var repository: ContactRepository
     val testCoroutineDispatcher=TestCoroutineDispatcher()
    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()
    val mockContext= mock(Context::class.java)
    private  val contactDao:ContactDao= Mockito.spy(Room.inMemoryDatabaseBuilder(mockContext,
            AppDataBase::class.java).allowMainThreadQueries().build()).getContactDao()
   private val dao=mock(contactDao::class.java)

     @Before
    fun setUp(){
     Dispatchers.setMain(testCoroutineDispatcher)

    }

    @Test
    fun should_call_repository_saving(){
         val contact = TestUtil.createContact()
         val repository=mock(ContactRepository(contactDao)::class.java)
        val viewModel=MainViewModel( ContactRepository(contactDao))
          testScope.launch {
              viewModel.saveContact(contact)
              verify(repository).insertContact(contact)
          }
          testScope.advanceUntilIdle()

    }
    @Test
    fun should_not_empty_when_exist_data(){
        val contact = TestUtil.createContact()
        val contactOne = TestUtil.createContact()
        val contactTwo = TestUtil.createContact()
        val repository=mock(ContactRepository(contactDao)::class.java)
        val viewModel=MainViewModel( ContactRepository(contactDao))
        testScope.launch {
            contactDao.insertContact(contact,contactOne,contactTwo)
        }
        testScope.advanceUntilIdle()

        testScope.launch {
          val  list=viewModel.contactLists
            list.observeForever {
                assertEquals(3,it.size)
            }
            testScope.advanceUntilIdle()
        }

    }
   @After
   fun teardown(){
       Dispatchers.resetMain()
       testCoroutineDispatcher.cleanupTestCoroutines()
   }
}