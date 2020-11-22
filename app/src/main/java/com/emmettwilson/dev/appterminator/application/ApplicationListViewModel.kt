package com.emmettwilson.dev.appterminator.application

import androidx.lifecycle.ViewModel

class ApplicationListViewModel(private val repo: ApplicationInfoRepository) : ViewModel() {

    fun getApplicationList() = repo.getDebugApplications()
}
