package com.example.bloomkitchen.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.example.bloomkitchen.R
import com.example.bloomkitchen.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getProfileData()
        setClickListener()
        editProfileData()

        profileViewModel.profileData.observe(viewLifecycleOwner, Observer { profile ->
            binding.profileData.ivProfilePhoto.load(profile.image) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
            binding.profileData.etUsername.setText(profile.username)
            binding.profileData.etEmail.setText(profile.email)
            binding.profileData.etPhoneNumber.setText(profile.phoneNumber.toString())
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
            binding.profileData.etPhoneNumber.isEnabled = it
        }
    }
}