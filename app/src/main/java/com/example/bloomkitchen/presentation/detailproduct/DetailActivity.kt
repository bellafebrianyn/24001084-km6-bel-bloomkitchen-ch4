package com.example.bloomkitchen.presentation.detailproduct

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.databinding.ActivityDetailBinding
import com.example.bloomkitchen.utils.proceedWhen
import com.example.bloomkitchen.utils.toIndonesianFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRAS_DETAIL_DATA = "EXTRAS_DETAIL_DATA"

        fun startActivity(
            context: Context,
            menu: Menu,
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRAS_DETAIL_DATA, menu)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val detailViewModel: DetailMenuViewModel by viewModel {
        parametersOf(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(detailViewModel.menu)
        setClickListener()
        navigateToGoogleMaps(detailViewModel.menu)
        observeData()
    }

    private fun observeData() {
        detailViewModel.priceLiveData.observe(this) {
            binding.layoutAddToCart.btnTotalPrice.isEnabled = it != 0.0
            binding.layoutAddToCart.btnTotalPrice.text =
                getString(
                    R.string.add_to_cart_price,
                    it.toIndonesianFormat(),
                )
        }
        detailViewModel.menuCountLiveData.observe(this) {
            binding.layoutAddToCart.tvQuantity.text = it.toString()
        }
    }

    private fun setClickListener() {
        binding.layoutDetailMenu.icBackToHome.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAddToCart.icDecrease.setOnClickListener {
            detailViewModel.minus()
        }
        binding.layoutAddToCart.icIncrease.setOnClickListener {
            detailViewModel.add()
        }
        binding.layoutAddToCart.btnTotalPrice.setOnClickListener {
            addProductToCart()
        }
    }

    private fun addProductToCart() {
        detailViewModel.addToCart().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to Cart Success", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, "Add to Cart Failed", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    Toast.makeText(this, "loading..", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun bindMenu(menu: Menu?) {
        menu?.let { item ->
            binding.layoutDetailMenu.ivDetailMenu.load(item.imageUrl) {
                crossfade(true)
            }
            binding.layoutDetailMenu.tvMenu.text = item.name
            binding.layoutDetailMenu.tvPrice.text = item.price.toIndonesianFormat()
            binding.layoutDetailMenu.tvDescMenu.text = item.detail
            binding.layoutDetailLocation.tvDetailLocation.text = menu.restoAddress
            binding.layoutAddToCart.btnTotalPrice.text =
                getString(
                    R.string.add_to_cart_price,
                    menu.price.toIndonesianFormat(),
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
                openGoogleMapsLocation(item.locationUrl)
            }
        }
    }
}
