package com.example.bloomkitchen.presentation.checkout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDatabaseDataSource
import com.example.bloomkitchen.data.datasouce.menu.MenuApiDataSource
import com.example.bloomkitchen.data.datasouce.menu.MenuDataSource
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CartRepositoryImpl
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.data.repository.MenuRepositoryImpl
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.data.source.local.database.AppDatabase
import com.example.bloomkitchen.data.source.network.service.BloomKitchenApiService
import com.example.bloomkitchen.databinding.ActivityCheckoutBinding
import com.example.bloomkitchen.databinding.LayoutDialogOrderBinding
import com.example.bloomkitchen.presentation.checkout.adapter.PriceListAdapter
import com.example.bloomkitchen.presentation.common.adapter.CartListAdapter
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.proceedWhen
import com.example.bloomkitchen.utils.toIndonesianFormat
import java.text.SimpleDateFormat
import java.util.Date

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val viewModel: CheckoutViewModel by viewModels {
        val s = BloomKitchenApiService.invoke()
        val db = AppDatabase.getInstance(this)
        val ds: CartDataSource = CartDatabaseDataSource(db.cartDao())
        val cartRepository: CartRepository = CartRepositoryImpl(ds)
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        val menuDataSource: MenuDataSource = MenuApiDataSource(s)
        val menuRepository: MenuRepository = MenuRepositoryImpl(menuDataSource, userRepository)
        GenericViewModelFactory.create(CheckoutViewModel(cartRepository,userRepository, menuRepository))
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
    }

    private fun setActionOnSuccessOrder() {
        viewModel.checkoutData.observe(this) { result ->
            result.proceedWhen (
                doOnSuccess = {
                    binding.layoutSectionCheckout.btnCheckout.setOnClickListener {
                        showDialog()
                    }
                }
            )
        }
    }

    private fun showDialog() {
            val binding: LayoutDialogOrderBinding = LayoutDialogOrderBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(this).create()

            val dateAndTime = SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(Date())
            binding.tvTransactionDate.text = dateAndTime

            val generatedNumber = NumberGenerator.generateNextNumber()
            binding.tvTransactionNumber.text = generatedNumber

            binding.rvSummaryOrder.adapter = priceItemAdapter

            binding.btnBackToHome.setOnClickListener {
                viewModel.deleteAllCarts()
                dialog.dismiss()
                finish()
            }

            dialog.apply {
                setView(binding.root)
                show()
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
        binding.layoutSectionCheckout.btnCheckout.setOnClickListener {
            doCheckout()
        }
    }

    private fun doCheckout() {
        viewModel.checkoutCart().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = true
                    binding.layoutContent.rvCart.isVisible = true
                    viewModel.deleteAllCarts()
                    setActionOnSuccessOrder()
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    Toast.makeText(this,
                        getString(R.string.error_checkout),
                        Toast.LENGTH_SHORT).show()
                }
            )
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