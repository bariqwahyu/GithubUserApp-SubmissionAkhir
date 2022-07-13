package com.bariq.bariqgithub3.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bariq.bariqgithub3.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM githubUser where username = :username")
    fun getUser(username: String): LiveData<UserEntity>

    @Query("SELECT * FROM githubUser where favorite = 1")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(users: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM githubUser WHERE favorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM githubUser WHERE username = :username AND favorite = 1)")
    suspend fun isUserFavorite(username: String?): Boolean
}