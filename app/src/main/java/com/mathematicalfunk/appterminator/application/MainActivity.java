package com.mathematicalfunk.appterminator.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mathematicalfunk.appterminator.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ProcessViewHolderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);


        adapter = new ProcessViewHolderAdapter(activityManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);

        List<ApplicationInfo> debugApps = new ArrayList<>();

        for (ApplicationInfo applicationInfo : installedApplications){
            boolean isDebuggable =  ( 0 != ( applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if(isDebuggable){
                debugApps.add(applicationInfo);
            }
        }

        adapter.addProcesses(debugApps);
    }
}
