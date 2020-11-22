package com.emmettwilson.dev.appterminator.application

import android.app.ActivityManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class ApplicationInfoRepositoryImplTest{

    private val activityManager = mockk<ActivityManager>()
    private val packageManager = mockk<PackageManager>()

    private val testObject = ApplicationInfoRepositoryImpl(packageManager)

    @Test
    fun `GIVEN package manager returns no appliactions WHEN getDebugApplications called THEN return empty list`() {
        every { packageManager.getInstalledApplications(0) } returns listOf()

        assertThat(testObject.getDebugApplications()).isEmpty()
    }

    @Test
    fun `GIVEN package manager returns only release applications WHEN getDebugApplications called THEN return empty list`() {
        val notDebuggableApp = ApplicationInfo().apply {
            flags = ApplicationInfo.FLAG_INSTALLED
        }

        every { packageManager.getInstalledApplications(0) } returns listOf(notDebuggableApp)

        assertThat(testObject.getDebugApplications()).isEmpty()
    }

    @Test
    fun `GIVEN package manager returns only a debuggable application WHEN getDebugApplications called THEN return application info`() {
        val debuggableApp = ApplicationInfo().apply {
            flags = ApplicationInfo.FLAG_INSTALLED or ApplicationInfo.FLAG_DEBUGGABLE
        }

        val notDebuggableApp = ApplicationInfo().apply {
            flags = ApplicationInfo.FLAG_INSTALLED
        }

        every { packageManager.getInstalledApplications(0) } returns listOf(debuggableApp, notDebuggableApp)

        assertThat(testObject.getDebugApplications()).contains(debuggableApp)
    }
}