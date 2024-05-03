package com.example.bloomkitchen.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import coil.load
import com.example.bloomkitchen.R
import com.example.bloomkitchen.databinding.FragmentProfileBinding
import com.example.bloomkitchen.presentation.login.LoginActivity
import com.example.bloomkitchen.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getProfileData()
        setClickListener()
        editProfileData()
        doLogout()

        profileViewModel.profileData.observe(
            viewLifecycleOwner,
            Observer { profile ->
                binding.profileData.ivProfilePhoto.load(profile.image) {
                    crossfade(true)
                    error(R.mipmap.ic_launcher)
                }
                val currentUser = profileViewModel.getCurrentUser()
                binding.profileData.etUsername.setText("${currentUser?.fullName}")
                binding.profileData.etEmail.setText("${currentUser?.email}")
            },
        )
    }

    private fun setClickListener() {
        binding.profileHeader.ivEdit.setOnClickListener {
            profileViewModel.changeEditMode()
            profileViewModel.editProfile.observe(viewLifecycleOwner) { iseditMode ->
                binding.btnSave.isVisible = iseditMode
            }
        }
        binding.btnSave.setOnClickListener {
            confirmUserDataUpdate()
        }
        binding.btnChangePassword.setOnClickListener {
            confirmUserPasswordUpdate()
        }
    }

    private fun confirmUserPasswordUpdate() {
        val dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.confirm_update_password))
                .setPositiveButton(
                    "Ya",
                ) { dialog, id ->
                    profileViewModel.reqChangePasswordByEmail()
                    requestChangePasswordDialog()
                }
                .setNegativeButton(
                    "Tidak",
                ) { dialog, id ->
                }.create()
        dialog.show()
    }

    private fun requestChangePasswordDialog() {
        val dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.dialog_dialog_req_update_password))
                .setPositiveButton(
                    getString(R.string.oke),
                ) { dialog, id ->
                }.create()
        dialog.show()
    }

    private fun confirmUserDataUpdate() {
        val dialog =
            AlertDialog.Builder(requireContext()).setMessage(getString(R.string.confirm_update))
                .setPositiveButton(
                    getString(R.string.yes),
                ) { dialog, id ->
                    updateFullName()
                }
                .setNegativeButton(
                    getString(R.string.no),
                ) { dialog, id ->
                }.create()
        dialog.show()
    }

    private fun updateFullName() {
        if (isFullNameValid()) {
            val newFullName = binding.profileData.etUsername.text.toString().trim()
            updateFullNameProcess(newFullName)
        }
    }

    private fun updateFullNameProcess(newFullName: String) {
        profileViewModel.updateProfile(newFullName).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.btnSave.isVisible = false
                    Toast.makeText(requireContext(), getString(R.string.accept_update), Toast.LENGTH_SHORT).show()
                    profileViewModel.changeEditMode()
                },
                doOnError = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_error, it.exception?.message),
                        Toast.LENGTH_SHORT,
                    ).show()
                },
            )
        }
    }

    private fun isFullNameValid(): Boolean {
        val newFullName = binding.profileData.etUsername.text.toString().trim()
        return fullNameValidation(newFullName)
    }

    private fun fullNameValidation(newFullName: String): Boolean {
        val currentFullName = profileViewModel.getCurrentUser()
        return if (newFullName == currentFullName?.fullName) {
            binding.profileData.username.isErrorEnabled = true
            binding.profileData.etUsername.error = getString(R.string.error_name_is_not_change)
            false
        } else {
            binding.profileData.username.isErrorEnabled = false
            true
        }
    }

    private fun editProfileData() {
        profileViewModel.editProfile.observe(viewLifecycleOwner) { isEditMode ->
            binding.profileData.etUsername.isEnabled = isEditMode
            binding.profileData.etEmail.isEnabled = false
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
            },
        )
    }

    private fun doLogout() {
        binding.btnLogout.setOnClickListener {
            profileViewModel.isUserLoggedOut()
            navigateToLogin()

            requireActivity().supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE,
            )
        }
    }
}
