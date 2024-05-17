package com.example.bloomkitchen.data.datasouce.userpreference

import com.example.bloomkitchen.data.source.local.pref.UserPreference
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserPreferenceDataSourceImplTest {
    @MockK
    lateinit var userPreference: UserPreference
    lateinit var dataSource: UserPreferenceDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = UserPreferenceDataSourceImpl(userPreference)
    }

    @Test
    fun isUsingGridMode() {
        every {
            dataSource.isUsingGridMode()
        } returns true
        val actualResult = userPreference.isUsingGridMode()
        verify { dataSource.isUsingGridMode() }
        assertEquals(true, actualResult)
    }

    @Test
    fun setUsingGridMode() {
        every {
            dataSource.setUsingGridMode(any())
        } returns Unit
        userPreference.setUsingGridMode(true)
        verify { dataSource.setUsingGridMode(any()) }
    }
}
