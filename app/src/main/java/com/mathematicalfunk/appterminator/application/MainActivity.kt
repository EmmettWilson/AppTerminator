package com.mathematicalfunk.appterminator.application

import android.app.ActivityManager
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mathematicalfunk.appterminator.R
import java.util.*

class MainActivity : AppCompatActivity() {
    private var adapter: ProcessViewHolderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val activityManager = applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        adapter = ProcessViewHolderAdapter(activityManager)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val packageManager = packageManager
        val installedApplications = packageManager.getInstalledApplications(0)
        val debugApps: MutableList<ApplicationInfo> = ArrayList()
        for (applicationInfo in installedApplications) {
            val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
            if (isDebuggable) {
                debugApps.add(applicationInfo)
            }
        }
        adapter!!.addProcesses(debugApps)
    }
}