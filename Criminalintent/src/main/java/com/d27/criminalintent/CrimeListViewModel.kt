package com.d27.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel(){

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
}