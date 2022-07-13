package com.bariq.bariqgithub3.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "githubUser")
class UserEntity(

    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url: String? = null,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "repository")
    val repository: Int? = null,

    @field:ColumnInfo(name = "company")
    val company: String? = null,

    @field:ColumnInfo(name = "location")
    val location: String? = null,

    @field:ColumnInfo(name = "followers")
    val followers: Int? = null,

    @field:ColumnInfo(name = "following")
    val following: Int? = null,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean
)