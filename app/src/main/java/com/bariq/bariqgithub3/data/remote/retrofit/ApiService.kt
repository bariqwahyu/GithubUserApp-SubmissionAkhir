package com.bariq.bariqgithub3.data.remote.retrofit

import com.bariq.bariqgithub3.BuildConfig
import com.bariq.bariqgithub3.data.remote.response.DetailUserResponse
import com.bariq.bariqgithub3.data.remote.response.ItemsItem
import com.bariq.bariqgithub3.data.remote.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    fun getSearch(@Query("q") username: String): Call<SearchResponse>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): DetailUserResponse

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}