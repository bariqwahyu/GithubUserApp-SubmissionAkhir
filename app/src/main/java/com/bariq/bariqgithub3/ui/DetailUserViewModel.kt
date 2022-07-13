package com.bariq.bariqgithub3.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bariq.bariqgithub3.Event
import com.bariq.bariqgithub3.data.UserRepository
import com.bariq.bariqgithub3.data.local.entity.UserEntity
import com.bariq.bariqgithub3.data.remote.response.ItemsItem
import com.bariq.bariqgithub3.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers : LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following : LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun getUser(username: String) = userRepository.getUser(username)

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFavoriteUser() = userRepository.getFavoriteUser()

    fun saveUserFavorite(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(user, true)
        }
    }

    fun deleteUserFavorite(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(user, false)
        }
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}