package com.example.bloomkitchen.presentation.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.databinding.ActivityRegisterBinding
import com.example.bloomkitchen.presentation.main.MainActivity
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.highLightWord
import com.example.bloomkitchen.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val viewModel: RegisterViewModel by viewModels {
        val s: FirebaseService = FirebaseServiceImpl()
        val ds: AuthDataSource = FirebaseAuthDataSource(s)
        val r: UserRepository = UserRepositoryImpl(ds)
        GenericViewModelFactory.create(RegisterViewModel(r))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.layoutFormRegister.btnRegister.setOnClickListener {
            doRegister()
        }
        binding.layoutFormRegister.tvNavToLogin.highLightWord(getString(R.string.text_highlight_login_here)) {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        /*  startActivity(Intent(this, LoginActivity::class.java).apply {
              flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
          })*/
    }


    private fun doRegister() {
        if (isFormValid()) {
            val username = binding.layoutFormRegister.etUsername.text.toString().trim()
            val password = binding.layoutFormRegister.etPassword.text.toString().trim()
            val email = binding.layoutFormRegister.etEmail.text.toString().trim()
            val phoneNumber = binding.layoutFormRegister.etPhoneNumber.text.toString().trim()
            proceedRegister(username, password, email, phoneNumber)
        }
    }

    private fun proceedRegister(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ) {
        viewModel.doRegister(username, password, email, phoneNumber).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutFormRegister.pbLoading.isVisible = false
                    binding.layoutFormRegister.btnRegister.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.layoutFormRegister.pbLoading.isVisible = false
                    binding.layoutFormRegister.btnRegister.isVisible = true
                    Toast.makeText(
                        this,
                        "Login Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.layoutFormRegister.pbLoading.isVisible = true
                    binding.layoutFormRegister.btnRegister.isVisible = false
                }
            )
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun isFormValid(): Boolean {
        val username = binding.layoutFormRegister.etUsername.text.toString().trim()
        val password = binding.layoutFormRegister.etPassword.text.toString().trim()
        val confirmPassword = binding.layoutFormRegister.etConfirmPassword.text.toString().trim()
        val email = binding.layoutFormRegister.etEmail.text.toString().trim()
        val phoneNumber = binding.layoutFormRegister.etPhoneNumber.text.toString().trim()

        return checkNameValidation(username) && checkPasswordValidation(
            password,
            binding.layoutFormRegister.tilPassword
        ) &&
                checkEmailValidation(email) && checkPasswordValidation(
            confirmPassword,
            binding.layoutFormRegister.tilConfirmPassword
        ) &&
                checkPwdAndConfirmPwd(password, confirmPassword)
    }

    private fun checkPwdAndConfirmPwd(password: String, confirmPassword: String): Boolean {
        return if (password != confirmPassword) {
            binding.layoutFormRegister.tilPassword.isErrorEnabled = true
            binding.layoutFormRegister.tilPassword.error =
                getString(R.string.text_password_does_not_match)
            binding.layoutFormRegister.tilConfirmPassword.isErrorEnabled = true
            binding.layoutFormRegister.tilConfirmPassword.error =
                getString(R.string.text_password_does_not_match)
            false
        } else {
            binding.layoutFormRegister.tilPassword.isErrorEnabled = false
            binding.layoutFormRegister.tilConfirmPassword.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormRegister.tilEmail.isErrorEnabled = true
            binding.layoutFormRegister.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormRegister.tilEmail.isErrorEnabled = true
            binding.layoutFormRegister.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutFormRegister.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun checkPasswordValidation(
        confirmPassword: String,
        textInputLayout: TextInputLayout
    ): Boolean {
        return if (confirmPassword.isEmpty()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error =
                getString(R.string.text_error_password_empty)
            false
        } else if (confirmPassword.length < 8) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error =
                getString(R.string.text_error_password_less_than_8_char)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun checkNameValidation(username: String): Boolean {
        return if (username.isEmpty()) {
            binding.layoutFormRegister.tilUsername.isErrorEnabled = true
            binding.layoutFormRegister.tilUsername.error =
                getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutFormRegister.tilUsername.isErrorEnabled = false
            true
        }
    }

    private fun setupForm() {
        with(binding.layoutFormRegister) {
            tilUsername.isVisible = true
            tilPassword.isVisible = true
            tilEmail.isVisible = true
            tilConfirmPassword.isVisible = true
        }
    }
}