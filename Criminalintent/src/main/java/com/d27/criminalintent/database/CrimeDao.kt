package com.d27.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.d27.criminalintent.Crime
import java.util.*

@Dao
interface CrimeDao{
    @Query("Select * from crime")
    fun getCrimes():LiveData<List<Crime>>

    @Query("Select * from crime where id = (:id)")
    fun getCrime(id : UUID) :LiveData<Crime>
}