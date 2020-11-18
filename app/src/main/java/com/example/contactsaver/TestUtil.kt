package com.example.contactsaver

import com.example.contactsaver.models.Contact
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object TestUtil {
    private fun randomString()=UUID.randomUUID().toString()
    private fun randomInt()=ThreadLocalRandom.current().nextInt(0,1000 + 1)
    fun createContact(): Contact {
        return Contact(
            randomInt(),
            randomString(),
            randomString()
        )
    }
}