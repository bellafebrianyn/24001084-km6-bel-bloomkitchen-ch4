package com.example.bloomkitchen.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.databinding.ActivityMainBinding
import com.example.bloomkitchen.presentation.login.LoginActivity
import com.example.bloomkitchen.presentation.profile.ProfileViewModel
import com.example.bloomkitchen.utils.GenericViewModelFactory

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: MainViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(MainViewModel(userRepository))
    }

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
                    if(!viewModel.isUserLoggedIn()){
                        navigateToLogin()
                        controller.navigate(R.id.menu_tab_home)
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

}