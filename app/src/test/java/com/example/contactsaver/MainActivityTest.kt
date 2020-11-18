package com.example.contactsaver

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.room.Room
import com.example.contactsaver.database.AppDataBase
import com.example.contactsaver.models.Contact
import com.example.contactsaver.repository.ContactRepository
import com.example.contactsaver.viewmodels.MainViewModel
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainActivityTest{

   private val testScope= TestCoroutineScope()
    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()
    val testCoroutineDispatcher=TestCoroutineDispatcher()
    val mockContext=mock(Context::class.java)
    private val contactDao= Mockito.spy(Room.inMemoryDatabaseBuilder(mockContext,AppDataBase::class.java).build()).getContactDao()
    private val repository=ContactRepository(contactDao)
    private val viewModel=MainViewModel(repository)

    @Before
    fun setUp(){
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun should_not_be_empty_list(){
        val observer= mock(Observer<List<Contact>>(){}::class.java)

        val contact=TestUtil.createContact()
        val contactOne=TestUtil.createContact()
        val contactTwo=TestUtil.createContact()

        testScope.launch {
           viewModel.saveContact(contact)
            viewModel.saveContact(contactOne)
            viewModel.saveContact(contactTwo)
        }
         testScope.advanceUntilIdle()

        testScope.launch {
           val result= liveData {
               emit(   contactDao.getAllContact())
           }
            result?.let{
                it.observeForever(observer)
            }

            verify(observer).onChanged(listOf(contact,contactOne,contactTwo))
        }
        testScope.advanceUntilIdle()


    }
    @Test
    fun is_empty_when_no_data(){

        testScope.launch {
            val result= liveData{
                emit(contactDao.getAllContact())

            }
            result.observeForever {
                 assertTrue(it.isEmpty())
            }
        }
        testScope.advanceUntilIdle()

    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}