package com.example.bloomkitchen.presentation.checkout

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDatabaseDataSource
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CartRepositoryImpl
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.data.source.local.database.AppDatabase
import com.example.bloomkitchen.databinding.ActivityCheckoutBinding
import com.example.bloomkitchen.presentation.checkout.adapter.PriceListAdapter
import com.example.bloomkitchen.presentation.common.adapter.CartListAdapter
import com.example.bloomkitchen.presentation.login.LoginActivity
import com.example.bloomkitchen.presentation.main.MainActivity
import com.example.bloomkitchen.presentation.profile.ProfileViewModel
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.proceedWhen
import com.example.bloomkitchen.utils.toIndonesianFormat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val viewModel: CheckoutViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        val ds: CartDataSource = CartDatabaseDataSource(db.cartDao())
        val cartRepository: CartRepository = CartRepositoryImpl(ds)
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(CheckoutViewModel(cartRepository, userRepository))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }

    private val priceItemAdapter: PriceListAdapter by lazy {
        PriceListAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
        setClickListeners()
        setActionOnSuccessOrder()
        isLogin()
    }

    private fun isLogin() {
        lifecycleScope.launch {
            if (!viewModel.isUserLoggedIn()) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun setActionOnSuccessOrder() {
        viewModel.checkoutData.observe(this) { result ->
            result.proceedWhen (
                doOnSuccess = {
                    binding.layoutSectionCheckout.btnCheckout.setOnClickListener {
                        val dialog = Dialog(this)
                        dialog.setContentView(R.layout.layout_dialog_order)
                        val layoutParams = WindowManager.LayoutParams()
                        layoutParams.copyFrom(dialog.window?.attributes)
                        dialog.window?.attributes = layoutParams

                        val dateAndTime = SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(Date())
                        val tvDate = dialog.findViewById<TextView>(R.id.tv_transaction_date)
                        tvDate.text = dateAndTime

                        val generatedNumber = NumberGenerator.generateNextNumber()
                        val tvNumber = dialog.findViewById<TextView>(R.id.tv_transaction_number)
                        tvNumber.text = generatedNumber

                        val rvSummaryOrder = dialog.findViewById<RecyclerView>(R.id.rv_summary_order)
                        rvSummaryOrder.adapter = priceItemAdapter
                        val btnBackToHome = dialog.findViewById<Button>(R.id.btn_back_to_home)
                        dialog.show()
                        btnBackToHome.setOnClickListener {
                            viewModel.deleteAllCarts()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            dialog.dismiss()
                            finish()
                        }

                    }
                }
            )
        }
    }

    object NumberGenerator {
        private var counter = 0

        fun generateNextNumber(): String {
            counter++
            return counter.toString()
        }
    }

    private fun setClickListeners() {
        binding.layoutOrderHeader.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
        binding.layoutContent.rvShoppingSummary.adapter = priceItemAdapter
    }

    private fun observeData() {
        viewModel.checkoutData.observe(this) { result ->
            result.proceedWhen(doOnSuccess = {
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.layoutSectionCheckout.cvSectionCheckout.isVisible = true
                result.payload?.let { (carts, priceItems, totalPrice) ->
                    adapter.submitData(carts)
                    binding.layoutSectionCheckout.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                    priceItemAdapter.submitData(priceItems)
                }
            }, doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.layoutSectionCheckout.cvSectionCheckout.isVisible = false
            }, doOnError = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.layoutSectionCheckout.cvSectionCheckout.isVisible = false
            }, doOnEmpty = { data ->
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                data.payload?.let { (_, _, totalPrice) ->
                    binding.layoutSectionCheckout.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                }
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.layoutSectionCheckout.cvSectionCheckout.isVisible = false
            })
        }
    }
}