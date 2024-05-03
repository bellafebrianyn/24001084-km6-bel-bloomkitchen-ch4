package com.example.bloomkitchen.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bloomkitchen.R
import com.example.bloomkitchen.databinding.ActivityMainBinding
import com.example.bloomkitchen.presentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navigationController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navigationController)
        navigationController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.menu_tab_profile -> {
                    if(!mainViewModel.isUserLoggedIn()){
                        navigateToLogin()
                        controller.popBackStack(R.id.menu_tab_home, false)
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

}