package com.d27.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel(){
//    val crimes = mutableListOf<Crime>()

//    init {
//        for(i in 0 until 100){
//            val crime = Crime()
//            crime.title = "Crime #$i"
//            crime.isSolved = i %2 ==0
//            crime.viewType = i % 2
//            crimes += crime
//        }
//    }
    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository!!.getCrimes()

}