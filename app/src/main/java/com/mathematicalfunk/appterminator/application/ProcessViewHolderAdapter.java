package com.mathematicalfunk.appterminator.application;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mathematicalfunk.appterminator.R;

import java.util.List;


public class ProcessViewHolderAdapter extends RecyclerView.Adapter<ProcessViewHolder> {
    private List<ApplicationInfo> applicationInfoList;
    private ActivityManager activityManager;

    public ProcessViewHolderAdapter(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public ProcessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.process_list_item, parent, false);
        return new ProcessViewHolder(view, activityManager);
    }

    @Override
    public void onBindViewHolder(ProcessViewHolder holder, int position) {
        ApplicationInfo applicationInfo = applicationInfoList.get(position);
        holder.bindView(applicationInfo);
    }

    @Override
    public int getItemCount() {
        return applicationInfoList.size();
    }

    public void addProcesses(final List<ApplicationInfo> applicationInfoList) {
        this.applicationInfoList = applicationInfoList;
        notifyDataSetChanged();
    }
}
