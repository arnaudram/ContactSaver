package com.example.contactsaver.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.contactsaver.R

class GeneralSettingFragment:PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general,rootKey)
    }
}