package com.emmettwilson.dev.appterminator.application

import android.app.ActivityManager
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emmettwilson.appterminator.R

class ProcessViewHolderAdapter(private val activityManager: ActivityManager) : RecyclerView.Adapter<ProcessViewHolder>() {
    private var applicationInfoList: List<ApplicationInfo>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.process_list_item, parent, false)
        return ProcessViewHolder(view, activityManager)
    }

    override fun onBindViewHolder(holder: ProcessViewHolder, position: Int) {
        val applicationInfo = applicationInfoList!![position]
        holder.bindView(applicationInfo)
    }

    override fun getItemCount(): Int {
        return applicationInfoList!!.size
    }

    fun addProcesses(applicationInfoList: List<ApplicationInfo>?) {
        this.applicationInfoList = applicationInfoList
        notifyDataSetChanged()
    }
}
