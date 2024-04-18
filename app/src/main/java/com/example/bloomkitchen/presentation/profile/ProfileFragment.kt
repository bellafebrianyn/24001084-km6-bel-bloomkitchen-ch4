package com.example.bloomkitchen.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.databinding.FragmentProfileBinding
import com.example.bloomkitchen.presentation.login.LoginActivity
import com.example.bloomkitchen.presentation.login.LoginViewModel
import com.example.bloomkitchen.utils.GenericViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    private val viewModel: ProfileViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(ProfileViewModel(userRepository))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getProfileData()
        setClickListener()
        editProfileData()
        doLogout()

        profileViewModel.profileData.observe(viewLifecycleOwner, Observer { profile ->
            binding.profileData.ivProfilePhoto.load(profile.image) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
            binding.profileData.etUsername.setText(profile.username)
            binding.profileData.etEmail.setText(profile.email)
        })
    }

    private fun setClickListener() {
        binding.profileHeader.ivEdit.setOnClickListener {
            profileViewModel.changeEditMode()
        }
    }

    private fun editProfileData() {
        profileViewModel.editProfile.observe(viewLifecycleOwner) {
            binding.profileData.etUsername.isEnabled = it
            binding.profileData.etEmail.isEnabled = it
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun doLogout() {
        binding.btnLogout.setOnClickListener {
            viewModel.isUserLoggedOut()

            navigateToLogin()
        }
    }
}