package dev.danilovteodoro.javapop.network.api

import dev.danilovteodoro.javapop.network.RepositoryHolder
import retrofit2.http.GET

interface RepositoryApi {

    @GET("search/repositories?q=language:Java&sort=stars&page=1")
    fun getRepositories():RepositoryHolder
}