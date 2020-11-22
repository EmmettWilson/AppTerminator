package com.emmettwilson.dev.appterminator.application

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.pm.ApplicationInfo
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emmettwilson.appterminator.R

class ProcessViewHolder(itemView: View, private val activityManager: ActivityManager) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.app_icon)
    private val nameView: TextView = itemView.findViewById(R.id.app_name)

    fun bindView(applicationInfo: ApplicationInfo) {
        nameView.text = applicationInfo.packageName
        imageView.setImageDrawable(applicationInfo.loadIcon(nameView.context.packageManager))
        itemView.setOnClickListener { v ->
            val message = v.context.getString(R.string.confirm_app_kill, applicationInfo.packageName)
            AlertDialog.Builder(v.context)
                    .setMessage(message)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        activityManager.killBackgroundProcesses(applicationInfo.packageName)
                        Toast.makeText(v.context, v.context.getString(R.string.kill_process, applicationInfo.packageName), Toast.LENGTH_SHORT).show()
                    }.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }.create().show()
        }
    }


}