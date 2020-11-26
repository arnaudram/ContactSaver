package com.example.contactsaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.workDataOf
import com.example.contactsaver.adapters.ContactAdapter
import com.example.contactsaver.database.AppDataBase
import com.example.contactsaver.models.Contact
import com.example.contactsaver.repository.ContactRepository
import com.example.contactsaver.repository.ContactRepositoryFlow
import com.example.contactsaver.viewmodels.MainViewModel
import com.example.contactsaver.viewmodels.MainViewModelFactory
import com.example.contactsaver.viewmodels.MainViewModelFactoryFlow
import com.example.contactsaver.viewmodels.MainViewModelFlow
import com.example.contactsaver.worker.CreateContactWorker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false)
        PreferenceManager.setDefaultValues(this,R.xml.pref_headers,false)


        val addContact=findViewById<TextView>(R.id.add_contact)

        val application= requireNotNull(this.application)
        val contactDao=AppDataBase.getDataBase(application).getContactDaoFlow()
        val contactRepositoryFlow=ContactRepositoryFlow(contactDao)
        val mainViewModelFactoryFlow=MainViewModelFactoryFlow(application,contactRepositoryFlow)
        val mainViewModelFlow by lazy {
            ViewModelProvider(this,mainViewModelFactoryFlow)[MainViewModelFlow::class.java]
        }
        val recyclerView:RecyclerView=findViewById(R.id.recyclerView)
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager

         mainViewModelFlow.contactLists.observe(this, Observer {
             val adapter= ContactAdapter(it)
                     print("MainActivity :${it.size}")
               recyclerView.adapter=adapter
             adapter.notifyDataSetChanged()
            recyclerView.setHasFixedSize(true)

         })
        addContact.setOnClickListener {
              //  val contact=Contact(name = "Winner", email = "winnersforever@gmail.com")
            val name=edit_name.text.toString()
            val email=edit_email.text.toString()
            if(name.isNotEmpty() and email.isNotEmpty()){
               // mainViewModelFlow.saveContact(Contact(name=name,email=email))
                saveWithWorker(Contact(name=name,email=email))
               // Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
                edit_email.text.clear()
                edit_name.text.clear()
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_settings->{inflateSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun inflateSettings() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    private fun saveWithWorker(contact: Contact) {
        val workManager=WorkManager.getInstance(application)
        val data= workDataOf("name" to contact.name,
        "email" to contact.email)
        val workRequest= OneTimeWorkRequestBuilder<CreateContactWorker>()
            .setInputData(data).build()

        workManager.enqueue(workRequest)
    }
}