package com.d27.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d27.criminalintent.database.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(uuid: UUID): LiveData<Crime> = crimeDao.getCrime(uuid)

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository? {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}