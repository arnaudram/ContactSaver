package com.example.contactsaver

import android.app.Application
import timber.log.Timber

class ContactSaverApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}