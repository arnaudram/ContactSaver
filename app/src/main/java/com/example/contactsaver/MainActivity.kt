package com.example.contactsaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsaver.adapters.ContactAdapter
import com.example.contactsaver.database.AppDataBase
import com.example.contactsaver.models.Contact
import com.example.contactsaver.repository.ContactRepository
import com.example.contactsaver.viewmodels.MainViewModel
import com.example.contactsaver.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addContact=findViewById<TextView>(R.id.add_contact)

        val application= requireNotNull(this.application)
        val contactDao=AppDataBase.getDataBase(application).getContactDao()
        val contactRepository=ContactRepository(contactDao)
        val mainViewModelFactory=MainViewModelFactory(application,contactRepository)
        val mainViewModel by lazy {
            ViewModelProvider(this,mainViewModelFactory)[MainViewModel::class.java]
        }
        val recyclerView:RecyclerView=findViewById(R.id.recyclerView)
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager

         mainViewModel.contactLists.observe(this, Observer {
             val adapter= ContactAdapter(it)

               recyclerView.adapter=adapter
             adapter.notifyDataSetChanged()
            recyclerView.setHasFixedSize(true)

         })
        addContact.setOnClickListener {
              //  val contact=Contact(name = "Winner", email = "winnersforever@gmail.com")
            val name=edit_name.text.toString()
            val email=edit_email.text.toString()
            if(name.isNotEmpty() and email.isNotEmpty()){
                mainViewModel.saveContact(Contact(name=name,email=email))
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
            }


        }
    }
}