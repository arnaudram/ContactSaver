package com.example.contactsaver.database

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.contactsaver.TestUtil
import com.example.contactsaver.models.Contact
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertSame
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP,Build.VERSION_CODES.O))
@ExperimentalCoroutinesApi
class AppDataBaseTest{
    private lateinit var contactDao:ContactDao
    private lateinit var appDataBase: AppDataBase

    @get:Rule
   val instantTaskExecutorRule=InstantTaskExecutorRule()

    val testCoroutineDispatcher=TestCoroutineDispatcher()
    @Before
    fun setUp(){
        Dispatchers.setMain(testCoroutineDispatcher)
        val context= ApplicationProvider.getApplicationContext<Context>()
        appDataBase=Room.inMemoryDatabaseBuilder(context,AppDataBase::class.java).build()
        contactDao=appDataBase.getContactDao()
    }
@Test
fun getAllContact_should_return_emptylist()= testCoroutineDispatcher.runBlockingTest{

    //val observer = mock(Observer<LiveData<List<Contact>>> {  }::class.java )
    val contactList=MutableLiveData<List<Contact>>()
    CoroutineScope(Dispatchers.IO).launch {
        val contacts=contactDao.getAllContact()
        contactList.postValue(contacts)
    }

    contactList.value?.isEmpty()?.let { Assert.assertTrue(it) }




}
    @Test
    fun getAllContact_should_return_list(){
        val contactOne= TestUtil.createContact()
        val contactTwo=TestUtil.createContact()

          CoroutineScope(testCoroutineDispatcher).launch{
              withContext(Dispatchers.IO) {
                  contactDao.insertContact(contactOne, contactTwo)

              }

          }
        
        CoroutineScope(testCoroutineDispatcher).launch{
            val contacts= MutableLiveData<List<Contact>>()
            withContext(Dispatchers.IO) {
                val  result=contactDao.getAllContact()
                contacts.value=result
                Assert.assertEquals(2, contacts.value?.size)
            }

        }
        // Assert.assertEquals(2, contactList.value?.size)
    }
    @Test
     fun when_given_name_should_find_users(){
        val contactOne= TestUtil.createContact()
        val contactTwo=TestUtil.createContact()
        val name=contactOne.name
         CoroutineScope(testCoroutineDispatcher).launch{
              withContext(Dispatchers.IO){
                  contactDao.insertContact(contactOne,contactTwo)
              }
         }
        CoroutineScope(testCoroutineDispatcher).launch {
            withContext(Dispatchers.IO){
                val result= contactDao.findByName(name)
                assertSame(contactOne,result.first())
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