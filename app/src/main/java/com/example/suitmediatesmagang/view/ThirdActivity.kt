package com.example.suitmediatesmagang.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitmediatesmagang.data.UserAdapter
import com.example.suitmediatesmagang.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupRecyclerView()
        setupViewModel()
        setupSwipeRefresh()
    }

    private fun setupBackButton() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter { user ->
            val intent = Intent()
            intent.putExtra("selected_user", "${user.firstName} ${user.lastName}")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val isCurrentlyLoading = viewModel.isLoading.value ?: false
                val isCurrentlyRefreshing = viewModel.isRefreshing.value ?: false

                if (!isCurrentlyLoading && !isCurrentlyRefreshing && lastVisible >= totalItemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        viewModel.users.observe(this) { users ->
            adapter.submitList(users)

            val isRefreshing = viewModel.isRefreshing.value ?: false
            binding.emptyState.visibility = if (users.isEmpty() && !isRefreshing) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.isRefreshing.observe(this) { isRefreshing ->
            val users = viewModel.users.value ?: emptyList()
            binding.emptyState.visibility = if (users.isEmpty() && !isRefreshing) View.VISIBLE else View.GONE
        }

        viewModel.loadUsers()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshUsers()
        }
    }
}