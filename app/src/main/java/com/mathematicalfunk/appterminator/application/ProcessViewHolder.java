package com.mathematicalfunk.appterminator.application;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mathematicalfunk.appterminator.R;


public class ProcessViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;
    private final TextView nameView;
    private final ActivityManager activityManager;

    public ProcessViewHolder(final View itemView, final ActivityManager activityManager) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.app_icon);
        nameView = (TextView) itemView.findViewById(R.id.app_name);
        this.activityManager = activityManager;
    }

    public void bindView(final ApplicationInfo applicationInfo) {
        nameView.setText(applicationInfo.packageName);
        imageView.setImageDrawable(applicationInfo.loadIcon(nameView.getContext().getPackageManager()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String message = v.getContext().getString(R.string.confirm_app_kill, applicationInfo.packageName);
                new AlertDialog.Builder(v.getContext())
                        .setMessage(message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activityManager.killBackgroundProcesses(applicationInfo.packageName);
                                Toast.makeText(v.getContext(), v.getContext().getString(R.string.kill_process, applicationInfo.packageName), Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

    }
}
