package com.bariq.bariqgithub3.di

import android.content.Context
import com.bariq.bariqgithub3.data.UserRepository
import com.bariq.bariqgithub3.data.local.room.UserDatabase
import com.bariq.bariqgithub3.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}