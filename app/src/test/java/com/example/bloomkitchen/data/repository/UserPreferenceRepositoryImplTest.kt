package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.datasouce.userpreference.UserPreferenceDataSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserPreferenceRepositoryImplTest {
    @MockK
    lateinit var datasource: UserPreferenceDataSource
    private lateinit var userPreferenceRepository: UserPreferenceRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userPreferenceRepository = UserPreferenceRepositoryImpl(datasource)
    }

    @Test
    fun isUsingGridMode() {
        every { datasource.isUsingGridMode() } returns true
        val actualResult = userPreferenceRepository.isUsingGridMode()
        verify { datasource.isUsingGridMode() }
        assertEquals(true, actualResult)
    }

    @Test
    fun setUsingGridMode() {
        every { datasource.setUsingGridMode(any()) } returns Unit
        userPreferenceRepository.setUsingGridMode(true)
        verify { datasource.setUsingGridMode(any()) }
    }
}
