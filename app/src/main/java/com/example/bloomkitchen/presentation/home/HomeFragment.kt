package com.example.bloomkitchen.presentation.home

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.datasouce.authentication.FirebaseAuthDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.datasouce.cart.CartDatabaseDataSource
import com.example.bloomkitchen.data.datasouce.category.CategoryApiDataSource
import com.example.bloomkitchen.data.datasouce.category.CategoryDataSource
import com.example.bloomkitchen.data.datasouce.menu.MenuApiDataSource
import com.example.bloomkitchen.data.datasouce.menu.MenuDataSource
import com.example.bloomkitchen.data.model.Category
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.repository.CartRepositoryImpl
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.CategoryRepositoryImpl
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.data.repository.MenuRepositoryImpl
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.data.repository.UserRepositoryImpl
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.example.bloomkitchen.data.source.firebase.FirebaseServiceImpl
import com.example.bloomkitchen.data.source.local.database.AppDatabase
import com.example.bloomkitchen.data.source.local.pref.UserPreference
import com.example.bloomkitchen.data.source.local.pref.UserPreferenceImpl
import com.example.bloomkitchen.data.source.network.service.BloomKitchenApiService
import com.example.bloomkitchen.databinding.FragmentHomeBinding
import com.example.bloomkitchen.presentation.detailproduct.DetailActivity
import com.example.bloomkitchen.presentation.home.adapter.CategoryListAdapter
import com.example.bloomkitchen.presentation.home.adapter.MenuAdapter
import com.example.bloomkitchen.presentation.home.adapter.OnItemClickedListener
import com.example.bloomkitchen.utils.GenericViewModelFactory
import com.example.bloomkitchen.utils.proceedWhen

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userPreference: UserPreference
    private var categories: List<Category>? = null
    private var menuList: List<Menu>? = null

    private val viewModel: HomeViewModel by viewModels {
        val service = BloomKitchenApiService.invoke()
        val database = AppDatabase.getInstance(requireContext())
        val cartDataSource: CartDataSource = CartDatabaseDataSource(database.cartDao())
        val categoryDataSource: CategoryDataSource = CategoryApiDataSource(service)
        val cartRepository = CartRepositoryImpl(cartDataSource)
        val firebaseService: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(firebaseService)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        val menuDataSource: MenuDataSource = MenuApiDataSource(service)
        val menuRepository: MenuRepository = MenuRepositoryImpl(menuDataSource, userRepository)
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)
        GenericViewModelFactory.create(
            HomeViewModel(
                categoryRepository,
                menuRepository,
                cartRepository,
                userRepository
            )
        )


    }

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {
            getMenuData(it.name)
        }
    }

    private var menuAdapter: MenuAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreferenceImpl(requireContext())
        val isUsingGridMode = userPreference.isUsingGridMode()
        setupCategoryList()
        loadDataCategory()
        loadDataMenu()
        bindMenuList(isUsingGridMode)
        setClickAction(isUsingGridMode)
        setIconFromPref(isUsingGridMode)
        setGreetingsName()
    }

    private fun loadDataMenu() {
        menuList?.let { bindMenu(it) } ?: getMenuData()
    }

    private fun loadDataCategory() {
        categories?.let { bindCategoryList(it) } ?: getCategoryData()
    }

    private fun setGreetingsName() {
        if (!viewModel.userIsLoggedIn()) {
            binding.layoutHeader.tvGreetings.text = getString(R.string.greetings_guest)
        } else {
            val currentUser = viewModel.getCurrentUser()
            binding.layoutHeader.tvGreetings.text =
                getString(R.string.greetings_name, currentUser?.fullName)
        }
    }

    private fun setupCategoryList() {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
    }

    private fun getCategoryData() {
        viewModel.getCategories().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutContentStateCategory.root.isVisible = false
                    binding.layoutContentStateCategory.pbLoading.isVisible = false
                    binding.layoutContentStateCategory.tvError.isVisible = false
                    binding.layoutContentStateCategory.tvError.isVisible = false
                    binding.rvCategory.isVisible = true
                    it.payload?.let { data ->
                        categories = data
                        bindCategoryList(data)
                    }
                    setMenuConstraint(false)
                },
                doOnError = {
                    binding.layoutContentStateCategory.root.isVisible = true
                    binding.layoutContentStateCategory.pbLoading.isVisible = true
                    binding.layoutContentStateCategory.tvError.isVisible = true
                    binding.layoutContentStateCategory.tvError.text =
                        it.exception?.message.orEmpty()
                    binding.rvCategory.isVisible = false
                    setMenuConstraint(true)
                },
                doOnEmpty = {
                    binding.layoutContentStateCategory.root.isVisible = true
                    binding.layoutContentStateCategory.pbLoading.isVisible = false
                    binding.layoutContentStateCategory.tvError.isVisible = true
                    binding.layoutContentStateCategory.tvError.text =
                        getString(R.string.category_is_empty)
                    binding.rvCategory.isVisible = false
                    setMenuConstraint(false)
                },
                doOnLoading = {
                    binding.layoutContentStateCategory.root.isVisible = true
                    binding.layoutContentStateCategory.pbLoading.isVisible = true
                    binding.layoutContentStateCategory.tvError.isVisible = false
                    binding.rvCategory.isVisible = false
                    setMenuConstraint(true)
                }
            )
        }
    }

    private fun setMenuConstraint(isLoading: Boolean) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clHome)

        if (isLoading) {
            constraintSet.connect(
                R.id.tv_menu,
                ConstraintSet.TOP,
                R.id.layout_content_state_category,
                ConstraintSet.BOTTOM
            )

        } else {
            constraintSet.connect(
                R.id.tv_menu,
                ConstraintSet.TOP,
                R.id.rv_category,
                ConstraintSet.BOTTOM
            )
        }
        constraintSet.applyTo(binding.clHome)
    }

    private fun bindCategoryList(category: List<Category>) {
        this.categories = category
        categoryAdapter.submitData(category)
    }

    private fun setClickAction(usingGrid: Boolean) {
        var isUsingGrid = usingGrid
        binding.ivChangeListMode.setOnClickListener {
            isUsingGrid = !isUsingGrid
            if (isUsingGrid)
                binding.ivChangeListMode.setImageResource(R.drawable.ic_list)
            else
                binding.ivChangeListMode.setImageResource(R.drawable.ic_grid)
            bindMenuList(isUsingGrid)
            userPreference.setUsingGridMode(isUsingGrid)
        }
    }

    private fun bindMenuList(isUsingGrid: Boolean) {
        val listMode = if (isUsingGrid) MenuAdapter.MODE_GRID else MenuAdapter.MODE_LIST
        menuAdapter =
            MenuAdapter(
                listMode = listMode,
                listener = object : OnItemClickedListener<Menu> {
                    override fun onItemClicked(item: Menu) {
                        navigateToDetail(item)
                    }

                    override fun onItemAddedToCart(item: Menu) {
                        viewModel.addItemToCart(item)
                    }

                })

        binding.rvMenuList.apply {
            adapter = this@HomeFragment.menuAdapter
            layoutManager = GridLayoutManager(requireContext(), if (isUsingGrid) 2 else 1)
        }
        loadDataMenu()
    }

    private fun setIconFromPref(isGridMode: Boolean) {
        if (isGridMode) {
            binding.ivChangeListMode.setImageResource(R.drawable.ic_list)
        } else {
            binding.ivChangeListMode.setImageResource(R.drawable.ic_grid)
        }
    }

    private fun getMenuData(categoryName: String? = null) {
        viewModel.getMenu(categoryName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutContentStateMenu.root.isVisible = false
                    binding.layoutContentStateMenu.pbLoading.isVisible = false
                    binding.layoutContentStateMenu.tvError.isVisible = false
                    binding.layoutContentStateMenu.tvError.isVisible = false
                    binding.rvMenuList.isVisible = true
                    it.payload?.let { data ->
                        menuList = data
                        bindMenu(data)
                    }
                },
                doOnError = {
                    binding.layoutContentStateMenu.root.isVisible = true
                    binding.layoutContentStateMenu.pbLoading.isVisible = true
                    binding.layoutContentStateMenu.tvError.isVisible = true
                    binding.layoutContentStateMenu.tvError.text =
                        it.exception?.message.orEmpty()
                    binding.rvMenuList.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutContentStateMenu.root.isVisible = true
                    binding.layoutContentStateMenu.pbLoading.isVisible = false
                    binding.layoutContentStateMenu.tvError.isVisible = true
                    binding.layoutContentStateMenu.tvError.text =
                        getString(R.string.text_on_menu_data_empty)
                    binding.rvMenuList.isVisible = false
                },
                doOnLoading = {
                    binding.layoutContentStateMenu.root.isVisible = true
                    binding.layoutContentStateMenu.pbLoading.isVisible = true
                    binding.layoutContentStateMenu.tvError.isVisible = false
                    binding.rvMenuList.isVisible = false
                }
            )
        }
    }

    private fun bindMenu(menu: List<Menu>) {
        this.menuList = menu
        menuAdapter?.submitData(menu)
    }

    private fun navigateToDetail(item: Menu) {
        DetailActivity.startActivity(requireContext(), item)
    }

}