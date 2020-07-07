package com.d27.criminalintent

import android.content.Context
import androidx.room.Room
import com.d27.criminalintent.database.CrimeDatabase
import com.d27.criminalintent.database.migration_1_2
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {
    private val database  = Room.databaseBuilder(context, CrimeDatabase::class.java, DATABASE_NAME)
            .addMigrations(migration_1_2)
            .build()
    private val crimeData = database.crimeDao()
    val executor = Executors.newSingleThreadExecutor()

    fun getCrimes() = crimeData.getCrimes()

    fun getCrime(id : UUID) = crimeData.getCrime(id)

    fun updateCrime(crime: Crime){
        executor.execute {
            crimeData.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime){
        executor.execute {
            crimeData.addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get() : CrimeRepository{
            return INSTANCE ?: throw IllegalArgumentException("CrimeRepository must be initialized")
        }
    }
}