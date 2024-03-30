package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.datasouce.menu.MenuDataSource
import com.example.bloomkitchen.data.model.Menu

interface MenuRepository {
    fun getMenuList(): List<Menu>
}

class MenuRepositoryImpl(private val dataSource: MenuDataSource) : MenuRepository {
    override fun getMenuList(): List<Menu> = dataSource.getMenuList()
}