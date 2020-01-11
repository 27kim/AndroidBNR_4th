package com.d27.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer

private const val ARG_CRIME_ID = "crime_id"
private const val TAG = "CrimeFragment"
private const val DIALOG_DATE = "DialogDate"

class CrimeFragment :Fragment(), DatePickerFragment.DatePickerListener{

    private lateinit var crime: Crime
    private lateinit var titleFiled : EditText
    private lateinit var dateButton : Button
    private lateinit var solvedCheckBox : CheckBox

    private val crimeDetailViewModel : CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    fun newInstance(crime: UUID): CrimeFragment{

        val args = Bundle().apply {
            putSerializable(ARG_CRIME_ID, crime)
        }
        return CrimeFragment().apply {
            arguments = args
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId : UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
        Log.d(TAG, "args bundle crime ID : $crimeId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleFiled = view.findViewById(R.id.crime_title)
        dateButton = view.findViewById(R.id.crime_date)
        solvedCheckBox = view.findViewById(R.id.crime_solved)

        dateButton.setOnClickListener {
            DatePickerFragment().apply {
                setListener(this@CrimeFragment)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(this,
            Observer {
                it?.let{
                    this.crime = it
                    updateUI(it)
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime.title = p0.toString()
            }

        }
        titleFiled.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply{
            setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked  }
        }
    }

    override fun onStop() {
        crimeDetailViewModel.saveCrime(crime)
        super.onStop()
    }

    private fun updateUI(crime: Crime) {
        titleFiled.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    override fun onDateSelected(y: Int, m: Int, d: Int) {
        dateButton.text = "$y $m $d"
    }
}