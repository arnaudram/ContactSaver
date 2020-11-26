package com.example.contactsaver

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.core.content.ContextCompat.startActivity
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var navHeader: View
    private lateinit var sharePref: SharedPreferences
    private lateinit var mainViewModelFlow: MainViewModelFlow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView=findViewById<NavigationView>(R.id.navigationview)



        val application= requireNotNull(this.application)
        val contactDao=AppDataBase.getDataBase(application).getContactDaoFlow()
        val contactRepositoryFlow=ContactRepositoryFlow(contactDao)
        val mainViewModelFactoryFlow=MainViewModelFactoryFlow(application,contactRepositoryFlow)
        mainViewModelFlow = ViewModelProvider(this,mainViewModelFactoryFlow)[MainViewModelFlow::class.java]

        navHeader = navigationView.getHeaderView(0)

        sharePref = PreferenceManager.getDefaultSharedPreferences(this)

        val listener= NavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.setmode->{
                    mainViewModelFlow.onSetMode(true)
                    true
                }
                R.id.settings-> {
                   mainViewModelFlow.onSetting(true)
                    true
                }
                else -> false
            }
        }

        navigationView.setNavigationItemSelectedListener (listener)

        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false)
        PreferenceManager.setDefaultValues(this,R.xml.pref_headers,false)


        val addContact=findViewById<TextView>(R.id.add_contact)

       mainViewModelFlow.setting.observe(this, Observer {
           Timber.i("on setting: $it")
           if (it){
               inflateSettings()
               mainViewModelFlow.onSetting(false)
           }
       })
       mainViewModelFlow.nightMode.observe(this, Observer {
           if (it){
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
           } else
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
       })
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



    override fun onResume() {
        super.onResume()

        val name= sharePref.getString("edit_name","set your name")
        val email=sharePref.getString("edit_email","youremail@gmail.com")
        val socialNetwork=sharePref.getString("select_social_network","https://www.google.com")
        Timber.i("preference name :$name")
       with(navHeader){
          this.findViewById<TextView>(R.id.pref_name).text=name
           findViewById<TextView>(R.id.pref_email).text=email
           findViewById<TextView>(R.id.pref_social_network).text=socialNetwork
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