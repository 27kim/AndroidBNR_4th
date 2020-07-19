package io.lab27.launcher

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_launcher.*

private const val TAG = "NerdLauncherActivity"

class LauncherActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        recyclerView = app_recycler_view
        recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
        }

        setupAdapter()
    }

    private fun setupAdapter() {
        val startUpIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val activities = packageManager.queryIntentActivities(startUpIntent, 0)
        activities.sortWith(Comparator { o1, o2 ->
            String.CASE_INSENSITIVE_ORDER.compare(
                o1.loadLabel(packageManager).toString(),
                o2.loadLabel(packageManager).toString()
            )

        })
        recyclerView.adapter = ActivityAdapter(activities)
        Log.i(TAG, "Found ${activities.size} activities")
    }

    private class ActivityHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val nameTextView = itemView as TextView
        private lateinit var resolveInfo: ResolveInfo

        init {
            nameTextView.setOnClickListener(this)
        }

        fun bindActivity(resolveInfo: ResolveInfo) {
            this.resolveInfo = resolveInfo
            val packageManager = itemView.context.packageManager
            val appName = resolveInfo.loadLabel(packageManager).toString()
            nameTextView.text = appName
        }

        override fun onClick(v: View) {
            val activityInto = resolveInfo.activityInfo

            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(activityInto.applicationInfo.packageName, activityInto.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val context = v.context

            context.startActivity(intent)
        }
    }

    private class ActivityAdapter(val activities: List<ResolveInfo>) :
        RecyclerView.Adapter<ActivityHolder>() {
        override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(container.context)
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, container, false)
            return ActivityHolder(view)
        }

        override fun getItemCount(): Int {
            return activities.size
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            holder.bindActivity(activities[position])
        }

    }
}