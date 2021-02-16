package dev.danilovteodoro.javapop.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.danilovteodoro.javapop.di.NetworkModule
import dev.danilovteodoro.javapop.network.api.RepositoryApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class RepositoryNetworkTest {

    private lateinit var repositoryApi: RepositoryApi

    @Before
    fun setup(){
        val retrofit = NetworkModule.provideRetrofit(NetworkModule.providesGson())
        repositoryApi = NetworkModule.provideRepositoryApi(retrofit)
    }

    @Test
    fun testGetRepository() = runBlocking{
        val repositoryHolder = repositoryApi.getRepositories()
        assertNotNull(repositoryHolder)
    }
}