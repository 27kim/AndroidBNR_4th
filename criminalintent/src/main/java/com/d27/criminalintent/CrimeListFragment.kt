package com.d27.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes : ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        recyclerView = view.findViewById(R.id.crime_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        updateUI()
        return view
    }

    private fun updateUI() {
        recyclerView.adapter = CrimeAdapter(crimeListViewModel.crimes)
    }

    companion object {
        const val TAG = "CrimeListFragment"

        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
        }
    }


    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            holder.bind(crimes[position])
        }

    }
}