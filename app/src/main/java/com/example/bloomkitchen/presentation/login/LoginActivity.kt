package com.example.bloomkitchen.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.bloomkitchen.databinding.ActivityLoginBinding
import com.example.bloomkitchen.presentation.main.MainActivity
import com.example.bloomkitchen.presentation.register.RegisterActivity
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.highLightWord
import com.example.bloomkitchen.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(LoginViewModel(userRepository))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        SetClickListeners()
    }

    private fun SetClickListeners() {
        binding.layoutFormLogin.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.layoutFormLogin.tvNavToRegister.highLightWord(getString(R.string.highlight_register)) {
            navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun doLogin() {
        if (isFormValid()) {
            val email = binding.layoutFormLogin.etEmail.text.toString().trim()
            val password = binding.layoutFormLogin.etPassword.text.toString().trim()

            proceedLogin(email, password)
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun proceedLogin(email: String, password: String) {
        viewModel.doLogin(email, password).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.layoutFormLogin.pbLoading.isVisible= false
                    binding.layoutFormLogin.btnLogin.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.layoutFormLogin.pbLoading.isVisible = false
                    binding.layoutFormLogin.btnLogin.isVisible = true
                    Log.d("proceedLogin", "proceedLogin: ${it.exception?.message}")
                    Toast.makeText(
                        this,
                        getString(R.string.login_failed, it.exception?.message.orEmpty()),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.layoutFormLogin.pbLoading.isVisible = true
                    binding.layoutFormLogin.btnLogin.isVisible = false
                }
            )
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.layoutFormLogin.etEmail.text.toString().trim()
        val password = binding.layoutFormLogin.etPassword.text.toString().trim()

        return checkEmailValidation(email) &&
                checkPasswordValidation(password, binding.layoutFormLogin.tilPassword)

    }

    private fun checkPasswordValidation(confirmPassword: String, textInputLayout: TextInputLayout): Boolean {
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

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormLogin.tilEmail.isErrorEnabled = true
            binding.layoutFormLogin.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormLogin.tilEmail.isErrorEnabled = true
            binding.layoutFormLogin.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutFormLogin.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun setupForm() {
        with(binding.layoutFormLogin) {
            tilEmail.isVisible = true
            tilPassword.isVisible = true
        }
    }
}