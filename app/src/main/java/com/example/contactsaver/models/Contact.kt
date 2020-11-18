package com.example.contactsaver.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var name:String,
    var email:String
)