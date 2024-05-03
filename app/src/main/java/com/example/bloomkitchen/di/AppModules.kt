package com.example.bloomkitchen.di

import android.content.SharedPreferences
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDatabaseDataSource
import com.example.bloomkitchen.data.datasouce.category.CategoryApiDataSource
import com.example.bloomkitchen.data.datasouce.category.CategoryDataSource
import com.example.bloomkitchen.data.datasouce.menu.MenuApiDataSource
import com.example.bloomkitchen.data.datasouce.menu.MenuDataSource
import com.example.bloomkitchen.data.datasouce.userpreference.UserPreferenceDataSource
import com.example.bloomkitchen.data.datasouce.userpreference.UserPreferenceDataSourceImpl
import com.example.bloomkitchen.data.datasource.ProfileDataSource
import com.example.bloomkitchen.data.datasource.ProfileDataSourceImpl
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CartRepositoryImpl
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.CategoryRepositoryImpl
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.data.repository.MenuRepositoryImpl
import com.example.bloomkitchen.data.repository.UserPreferenceRepository
import com.example.bloomkitchen.data.repository.UserPreferenceRepositoryImpl
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.data.source.local.database.AppDatabase
import com.example.bloomkitchen.data.source.local.database.dao.CartDao
import com.example.bloomkitchen.data.source.local.pref.UserPreference
import com.example.bloomkitchen.data.source.local.pref.UserPreferenceImpl
import com.example.bloomkitchen.data.source.network.service.BloomKitchenApiService
import com.example.bloomkitchen.presentation.cart.CartViewModel
import com.example.bloomkitchen.presentation.checkout.CheckoutViewModel
import com.example.bloomkitchen.presentation.detailproduct.DetailMenuViewModel
import com.example.bloomkitchen.presentation.home.HomeViewModel
import com.example.bloomkitchen.presentation.login.LoginViewModel
import com.example.bloomkitchen.presentation.main.MainViewModel
import com.example.bloomkitchen.presentation.profile.ProfileViewModel
import com.example.bloomkitchen.presentation.register.RegisterViewModel
import com.example.bloomkitchen.presentation.splashscreen.SplashViewModel
import com.example.bloomkitchen.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {

    private val networkModule =
        module {
            single<BloomKitchenApiService> { BloomKitchenApiService.invoke() }
        }

    private val firebaseModule = module {
        single<FirebaseService> { FirebaseServiceImpl() }
    }

    val localModule =
        module {
            single<AppDatabase> { AppDatabase.getInstance(androidContext()) }
            single<CartDao> { get<AppDatabase>().cartDao() }
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    UserPreferenceImpl.PREF_NAME,
                )
            }
            single<UserPreference> { UserPreferenceImpl(get()) }
        }

    private val dataSource =
        module {
            single<AuthDataSource> { FirebaseAuthDataSource(get()) }
            single<CartDataSource> { CartDatabaseDataSource(get()) }
            single<CategoryDataSource> { CategoryApiDataSource(get()) }
            single<MenuDataSource> { MenuApiDataSource(get()) }
            single<ProfileDataSource> { ProfileDataSourceImpl() }
            single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<CartRepository> { CartRepositoryImpl(get()) }
            single<CategoryRepository> { CategoryRepositoryImpl(get()) }
            single<MenuRepository> { MenuRepositoryImpl(get(), get()) }
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<UserPreferenceRepository> { UserPreferenceRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::HomeViewModel)
            viewModel { params ->
                DetailMenuViewModel(
                    extras = params.get(),
                    cartRepository = get(),
                )
            }
            viewModelOf(::CartViewModel)
            viewModelOf(::CheckoutViewModel)
            viewModelOf(::MainViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::ProfileViewModel)
            viewModelOf(::SplashViewModel)
        }

    val modules = listOf<Module>(networkModule, firebaseModule, localModule, dataSource, repository, viewModelModule)
}