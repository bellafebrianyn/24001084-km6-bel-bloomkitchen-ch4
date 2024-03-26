package com.example.bloomkitchen.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.category.DummyCategoryDataSource
import com.example.bloomkitchen.data.menu.DummyMenuDataSource
import com.example.bloomkitchen.data.model.Category
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.CategoryRepositoryImpl
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.data.repository.MenuRepositoryImpl
import com.example.bloomkitchen.databinding.FragmentHomeBinding
import com.example.bloomkitchen.presentation.detailproduct.DetailActivity
import com.example.bloomkitchen.presentation.home.adapter.CategoryListAdapter
import com.example.bloomkitchen.presentation.home.adapter.MenuAdapter
import com.example.bloomkitchen.presentation.home.adapter.OnItemClickedListener
import com.example.bloomkitchen.utils.GenericViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeViewModel by viewModels {

        val menuDataSource = DummyMenuDataSource()
        val menuRepository : MenuRepository = MenuRepositoryImpl(menuDataSource)

        val categoryDataSource = DummyCategoryDataSource()
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)

        GenericViewModelFactory.create(HomeViewModel(categoryRepository, menuRepository))
    }

    private val categoryAdapter: CategoryListAdapter by lazy {
        CategoryListAdapter {

        }
    }

    /*private val menuAdapter: MenuListAdapter by lazy {
        MenuListAdapter {

        }
    }*/

    private var adapter: MenuAdapter? = null
    private var isUsingGridMode: Boolean = true

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
        bindCategoryList(viewModel.getCategories())
        bindMenuList(true)
        setClickAction()
    }

    private fun bindCategoryList(data: List<Category>) {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
        categoryAdapter.submitData(data)
    }

    /*private fun bindMenuList(data: List<Menu>) {
        binding.rvMenuList.apply {
            adapter = this@HomeFragment.adapter
        }
        adapter?.submitData(data)
    }*/
    private fun setClickAction() {
        binding.ivChangeListMode.setOnClickListener {
            isUsingGridMode = !isUsingGridMode
            setIcon(isUsingGridMode)
            bindMenuList(isUsingGridMode)
        }
    }

    private fun setIcon(usingGridMode: Boolean) {
        binding.ivChangeListMode.setImageResource(if (usingGridMode) R.drawable.ic_list else R.drawable.ic_grid)
    }

    private fun bindMenuList(isUsingGrid: Boolean) {
        val listMode = if (isUsingGrid) MenuAdapter.MODE_GRID else MenuAdapter.MODE_LIST
        adapter =
            MenuAdapter(
                listMode = listMode,
                listener = object : OnItemClickedListener<Menu> {
                    override fun onItemClicked(item: Menu) {
                        navigateToDetail(item)
                    }

                })
        binding.rvMenuList.apply {
            adapter = this@HomeFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), if (isUsingGrid) 2 else 1)
        }
        adapter?.submitData(viewModel.getMenuList())
    }

    private fun navigateToDetail(item: Menu) {
        DetailActivity.startActivity(requireContext(), item)
    }

}