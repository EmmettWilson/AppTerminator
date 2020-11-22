package com.emmettwilson.dev.appterminator.wiring

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageManager

import com.emmettwilson.dev.appterminator.application.ApplicationInfoRepository
import com.emmettwilson.dev.appterminator.application.ApplicationInfoRepositoryImpl
import com.emmettwilson.dev.appterminator.application.ApplicationListViewModel
import com.emmettwilson.dev.appterminator.application.ProcessViewHolderAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ApplicationInfoRepository> { ApplicationInfoRepositoryImpl(get())  }

    factory<PackageManager> { androidContext().packageManager }

    factory { androidContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager }

    viewModel { ApplicationListViewModel(get()) }

    factory { ProcessViewHolderAdapter(get()) }
}
