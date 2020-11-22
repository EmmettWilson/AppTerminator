package com.emmettwilson.dev.appterminator.application

import android.content.pm.ApplicationInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk

import org.junit.Test

class ApplicationListViewModelTest{

    private val repo = mockk<ApplicationInfoRepository>()
    private val testObject = ApplicationListViewModel(repo)

    @Test
    fun `GIVEN debug application view model WHEN getApplicationList called THEN delegates to application info repository`() {
        val applicationInfo1 = ApplicationInfo().apply { name = "app1" }
        val applicationInfo2 = ApplicationInfo().apply { name = "app2" }
        val applicationInfo3 = ApplicationInfo().apply { name = "app3" }
        val expected = listOf(applicationInfo1, applicationInfo2, applicationInfo3)
        every { repo.getDebugApplications() } returns expected

        val actual = testObject.getApplicationList()

        assertThat(actual).isEqualTo(expected)
    }
}