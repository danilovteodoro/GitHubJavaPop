package dev.danilovteodoro.javapop.network

import com.google.gson.annotations.SerializedName

data class RepositoryHolder(
    @SerializedName("total_count")
    val totalCount:Long,
    @SerializedName("incomplete_results")
    val incompleteResults:Boolean,
    @SerializedName("items")
    val repositories:List<RepositoryNetwork>
)
