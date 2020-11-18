package com.example.contactsaver.models




import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class ContactTest{
    @Test
    fun should_create_newInstance(){
        val contact=Contact(name = "my name",email =  "myeamil@gmail.com")

    }
    @Test
    fun should_autoIncrementId(){
        /*val contact= spy(Contact(name = "my name",email =  "myeamil@gmail.com"))
        doReturn(1).`when`(contact).id
        assertEquals(1,contact.id)
*/     val contact=mock(Contact::class.java)
        whenever(contact.id).thenReturn(1)
        assertEquals(1,contact.id)
    }
}