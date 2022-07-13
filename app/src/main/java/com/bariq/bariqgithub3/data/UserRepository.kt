package com.bariq.bariqgithub3.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bariq.bariqgithub3.data.local.entity.UserEntity
import com.bariq.bariqgithub3.data.local.room.UserDao
import com.bariq.bariqgithub3.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
    ){

    fun getUser(username: String): LiveData<Result<UserEntity>> = liveData {
        emit(Result.Loading)
        try {
            val users = apiService.getUser(username)
            val isFavorite = userDao.isUserFavorite(users.login)
            userDao.deleteAll()
            userDao.insertUser(UserEntity(
                users.login,
                users.avatarUrl,
                users.name,
                users.publicRepos,
                users.company,
                users.location,
                users.followers,
                users.following,
                isFavorite
                ))
        } catch (e: Exception) {
            Log.d("UserRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<UserEntity>> = userDao.getUser(username).map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    suspend fun setFavoriteUser(user: UserEntity, favoriteState: Boolean) {
        user.isFavorite = favoriteState
        userDao.updateUser(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}