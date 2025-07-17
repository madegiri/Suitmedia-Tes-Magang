package com.example.suitmediatesmagang.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suitmediatesmagang.data.User
import com.example.suitmediatesmagang.data.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private var currentPage = 1
    private var totalPages = 1
    private var isLoadingData = false

    fun loadUsers() {
        if (isLoadingData) return

        isLoadingData = true
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getUsers(currentPage)
                totalPages = response.totalPages

                val currentList = _users.value ?: emptyList()
                _users.value = currentList + response.data

                currentPage++
            } catch (e: Exception) {

            } finally {
                isLoadingData = false
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun loadNextPage() {
        if (currentPage <= totalPages) {
            loadUsers()
        }
    }

    fun refreshUsers() {
        _isRefreshing.value = true
        currentPage = 1

        loadUsersForRefresh()
    }

    private fun loadUsersForRefresh() {
        if (isLoadingData) return

        isLoadingData = true
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getUsers(1)
                totalPages = response.totalPages

                _users.value = response.data
                currentPage = 2
            } catch (e: Exception) {

            } finally {
                isLoadingData = false
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }
}