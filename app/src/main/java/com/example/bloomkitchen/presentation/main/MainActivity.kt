package com.example.bloomkitchen.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bloomkitchen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navigationController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navigationController)
    }
}