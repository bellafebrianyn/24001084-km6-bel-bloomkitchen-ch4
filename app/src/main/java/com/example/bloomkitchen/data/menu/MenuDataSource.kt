package com.example.bloomkitchen.data.menu

import com.example.bloomkitchen.data.model.Menu

interface MenuDataSource {
    fun getMenuList(): List<Menu>
}