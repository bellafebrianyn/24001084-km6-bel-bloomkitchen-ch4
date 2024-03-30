package com.example.bloomkitchen

import android.app.Application
import com.example.bloomkitchen.data.source.local.database.AppDatabase

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}