package dev.danilovteodoro.javapop.network

import com.google.gson.annotations.SerializedName


data class RepositoryNetwork(
    @SerializedName("id")
    val id:Long,
    @SerializedName("name")
    val name:String,
    @SerializedName("description")
    val description:String,
    @SerializedName("owner")
    val owner:OwnerNetwork,
    @SerializedName("stargazers_count")
    val starCount:Long,
    @SerializedName("forks_count")
    val forksCount:Long
)
