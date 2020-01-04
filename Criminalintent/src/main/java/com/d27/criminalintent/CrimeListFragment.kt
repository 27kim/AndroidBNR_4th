package com.d27.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CrimeListFragment : Fragment() {

    private var callbacks : Callbacks? = null
    interface Callbacks{
        fun onCrimeSelected(crime : UUID)
    }

    lateinit var recyclerView: RecyclerView

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes : ${crimeListViewModel.crimeListLiveData.value?.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        recyclerView = view.findViewById(R.id.crime_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        //최초에는 데이터가 없기 때문에 빈 list 를 넘겨 줌
        //liveData 로 변경했기 때문에 데이터가 들어오면 observer 통해 변경 예
        recyclerView.adapter = CrimeAdapter(emptyList())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }

            }
        )
    }

    private fun updateUI(crimes: List<Crime>) {
        recyclerView.adapter = CrimeAdapter(crimes)
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
        val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            callbacks?.let {
                it.onCrimeSelected(crime.id)
            }
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            val pattern = "EEEE, MMM dd, yyyy."
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date = simpleDateFormat.format(crime.date)
            dateTextView.text = date
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
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