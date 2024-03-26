package com.example.bloomkitchen.data.category

import com.example.bloomkitchen.data.model.Category

class DummyCategoryDataSource: CategoryDataSource {
    override fun getCategories(): List<Category> {
        return mutableListOf(
            Category(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/category_img/img_nasi_lemak.png?raw=true",
                name = "Nasi"
            ),
            Category(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/category_img/img_noodle.png?raw=true",
                name = "Mie"
            ),
            Category(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/category_img/img_snack.png?raw=true",
                name = "Snack"
            ),
            Category(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/category_img/img_beverage.png?raw=true",
                name = "Minuman"
            ),
            Category(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/category_img/img_bread.png?raw=true",
                name = "Roti"
            ),
        )
    }

}