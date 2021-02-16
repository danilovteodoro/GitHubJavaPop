package dev.danilovteodoro.javapop.network

import com.google.gson.annotations.SerializedName

data class OwnerNetwork(
    @SerializedName("id")
    val id:Long,
    @SerializedName("login")
    val login:String,
    @SerializedName("avatar_url")
    val avatarUrl:String
)
