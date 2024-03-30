package com.example.bloomkitchen.data.datasouce.menu

import com.example.bloomkitchen.data.model.Menu

interface MenuDataSource {
    fun getMenuList(): List<Menu>
}