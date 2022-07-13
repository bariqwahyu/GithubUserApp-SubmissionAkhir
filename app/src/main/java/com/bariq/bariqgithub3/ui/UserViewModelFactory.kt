package com.bariq.bariqgithub3.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bariq.bariqgithub3.data.UserRepository
import com.bariq.bariqgithub3.di.Injection

class UserViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UserViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}