package com.example.bloomkitchen.presentation.detailproduct

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.databinding.ActivityDetailBinding
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

    private var currentQuantity = 1
    private var pricePerItem: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getIntentData()
        val menu: Menu? = intent.extras?.getParcelable(EXTRAS_DETAIL_DATA)
        pricePerItem = menu?.price ?: 0.0
        updateTotalPrice()
        setUpQuantityButton()
        backToHome()
    }

    private fun backToHome() {
        binding.layoutDetailMenu.icBackToHome.setOnClickListener {
            finish()
        }
    }

    private fun getIntentData() {
        intent.extras?.getParcelable<Menu>(EXTRAS_DETAIL_DATA)?.let {
            setMenuImage(it.imgUrl)
            setMenuData(it)
        }
    }

    private fun setMenuImage(imgResDrawable: String?) {
        imgResDrawable?.let { binding.layoutDetailMenu.ivDetailMenu.load(it) }
    }

    private fun setMenuData(menu: Menu) {
        binding.layoutDetailMenu.tvMenu.text = menu.name
        binding.layoutDetailMenu.tvPrice.text = menu.price.toIndonesianFormat()
        binding.layoutDetailMenu.tvDescMenu.text = menu.desc
        binding.layoutDetailLocation.tvDetailLocation.text = menu.location
        binding.layoutAddToCart.btnTotalPrice.setText(
            getString(
                R.string.add_to_cart_price,
                menu.price.toIndonesianFormat()
            ))
        navigateToGoogleMaps(menu)
    }

    private fun updateQuantity(delta: Int) {
        currentQuantity += delta
        if (currentQuantity < 1) {
            currentQuantity = 1
        }
        updateQuantityText()
    }

    private fun updateQuantityText() {
        binding.layoutAddToCart.tvQuantity.text = currentQuantity.toString()
    }

    private fun setUpQuantityButton() {
        binding.layoutAddToCart.icIncrease.setOnClickListener {
            updateQuantity(1)
            calculateTotalPrice()
        }
        binding.layoutAddToCart.icDecrease.setOnClickListener {
            updateQuantity(-1)
            calculateTotalPrice()
        }
    }

    private fun calculateTotalPrice() {
        val totalPrice = currentQuantity * pricePerItem
        binding.layoutAddToCart.btnTotalPrice.setText(
            getString(
                R.string.add_to_cart_total_price,
                totalPrice.toIndonesianFormat()
            ))
    }

    private fun updateTotalPrice() {
        binding.layoutAddToCart.btnTotalPrice.setOnClickListener {
            calculateTotalPrice()
        }
    }

    private fun openGoogleMapsLocation(location: String) {
        val gmmIntentUri = Uri.parse(location)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)

    }

    private fun navigateToGoogleMaps(menu: Menu) {
        binding.layoutDetailLocation.tvDetailLocation.setOnClickListener {
            openGoogleMapsLocation(menu.googleMapsLink)
        }
    }
}