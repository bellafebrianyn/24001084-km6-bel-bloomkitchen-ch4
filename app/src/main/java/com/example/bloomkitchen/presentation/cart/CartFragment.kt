package com.example.bloomkitchen.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDatabaseDataSource
import com.example.bloomkitchen.data.model.Cart
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CartRepositoryImpl
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.data.source.local.database.AppDatabase
import com.example.bloomkitchen.databinding.FragmentCartBinding
import com.example.bloomkitchen.presentation.checkout.CheckoutActivity
import com.example.bloomkitchen.presentation.common.adapter.CartListAdapter
import com.example.bloomkitchen.presentation.common.adapter.CartListener
import com.example.bloomkitchen.presentation.login.LoginActivity
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.hideKeyboard
import com.example.bloomkitchen.utils.proceedWhen
import com.example.bloomkitchen.utils.toIndonesianFormat

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private val viewModel: CartViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        val ds: CartDataSource = CartDatabaseDataSource(db.cartDao())
        val cartRepository: CartRepository = CartRepositoryImpl(ds)
        val firebaseService: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(firebaseService)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(CartViewModel(cartRepository, userRepository))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }

            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }

            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.removeCart(cart)
            }

            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.setCartNotes(cart)
                hideKeyboard()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        observeData()
        setClickListeners()
    }

    private fun setClickListeners() {
            binding.layoutSectionCheckout.btnCheckout.setOnClickListener {
                if (viewModel.isLoggedIn()) {
                    startActivity(Intent(requireContext(), CheckoutActivity::class.java))
                } else {
                    navigateToLogin()
                }
            }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun observeData() {
        viewModel.getAllCarts().observe(viewLifecycleOwner) { result ->
            result.proceedWhen (
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = false
                    binding.layoutSectionCheckout.btnCheckout.isEnabled = false
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = true
                    binding.layoutSectionCheckout.btnCheckout.isEnabled = true
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.layoutSectionCheckout.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                    }
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible =true
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.rvCart.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible =true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    binding.rvCart.isVisible = false
                    binding.layoutSectionCheckout.btnCheckout.isEnabled = false
                    result.payload?.let { (carts, totalPrice) ->
                        binding.layoutSectionCheckout.tvTotalPrice.text = totalPrice.toIndonesianFormat()
                    }
                }
            )
        }
    }

    private fun setupList() {
        binding.rvCart.adapter = this@CartFragment.adapter
    }

}