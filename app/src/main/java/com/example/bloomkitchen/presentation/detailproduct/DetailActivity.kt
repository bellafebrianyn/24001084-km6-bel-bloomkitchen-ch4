package com.example.bloomkitchen.presentation.detailproduct

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDatabaseDataSource
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CartRepositoryImpl
import com.example.bloomkitchen.data.source.local.database.AppDatabase
import com.example.bloomkitchen.databinding.ActivityDetailBinding
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.proceedWhen
import com.example.bloomkitchen.utils.toIndonesianFormat

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_DETAIL_DATA = "EXTRAS_DETAIL_DATA"

        fun startActivity(context: Context, menu: Menu) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRAS_DETAIL_DATA, menu)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailMenuViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        val ds: CartDataSource = CartDatabaseDataSource(db.cartDao())
        val rp: CartRepository = CartRepositoryImpl(ds)
        GenericViewModelFactory.create(
            DetailMenuViewModel(intent?.extras, rp)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(viewModel.menu)
        setClickListener()
        navigateToGoogleMaps(viewModel.menu)
        observeData()
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this) {
                binding.layoutAddToCart.btnTotalPrice.isEnabled = it != 0.0
            binding.layoutAddToCart.btnTotalPrice.text = getString(R.string.add_to_cart_price,
                it.toIndonesianFormat())
            }
        viewModel.menuCountLiveData.observe(this) {
            binding.layoutAddToCart.tvQuantity.text = it.toString()
        }
    }

    private fun setClickListener() {
        binding.layoutDetailMenu.icBackToHome.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAddToCart.icDecrease .setOnClickListener {
            viewModel.minus()
        }
        binding.layoutAddToCart.icIncrease.setOnClickListener {
            viewModel.add()
        }
        binding.layoutAddToCart.btnTotalPrice.setOnClickListener {
            addProductToCart()
        }
    }

    private fun addProductToCart() {
        viewModel.addToCart().observe(this) {
            it.proceedWhen (
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, "Add to cart failed", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    Toast.makeText(this, "loading..", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun bindMenu(menu: Menu?) {
        menu?.let { item ->
            binding.layoutDetailMenu.ivDetailMenu.load(item.imgUrl) {
                crossfade(true)
            }
            binding.layoutDetailMenu.tvMenu.text = menu.name
            binding.layoutDetailMenu.tvPrice.text = menu.price.toIndonesianFormat()
            binding.layoutDetailMenu.tvDescMenu.text = menu.desc
            binding.layoutDetailLocation.tvDetailLocation.text = menu.location
            binding.layoutAddToCart.btnTotalPrice.text=
                getString(
                    R.string.add_to_cart_price,
                    menu.price.toIndonesianFormat()
                )
        }
    }

    private fun openGoogleMapsLocation(location: String) {
        val gmmIntentUri = Uri.parse(location)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)

    }

    private fun navigateToGoogleMaps(menu: Menu?) {
        menu?.let { item ->
            binding.layoutDetailLocation.tvDetailLocation.setOnClickListener {
                openGoogleMapsLocation(item.googleMapsLink)
            }
        }
    }
}