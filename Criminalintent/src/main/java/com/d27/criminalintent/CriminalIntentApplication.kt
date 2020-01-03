package com.d27.criminalintent

import android.app.Application
import com.facebook.stetho.Stetho

class CriminalIntentApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
        Stetho.initializeWithDefaults(this)
    }
}