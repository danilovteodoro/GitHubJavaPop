package dev.danilovteodoro.javapop.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.network.api.RepositoryApi
import dev.danilovteodoro.javapop.network.mapper.RepositoryNetworkMapper
import dev.danilovteodoro.javapop.util.AndroidUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import util.DataState
import javax.inject.Inject

class GitRepoRepository @Inject constructor(
    val api: RepositoryApi,
    val networkMapper:RepositoryNetworkMapper,
    @ApplicationContext val context: Context
){

    fun getRepositories(): Flow<DataState<List<Repository>>> = flow{
        emit(DataState.Loading)
        if(!AndroidUtil.isNetworkConnected(context)){
            emit(DataState.NoConnection)
            return@flow
        }
        val repositoryHolder = api.getRepositories()
        val gitRepos =  networkMapper.mapFromEntityList(repositoryHolder.repositories)
        emit(DataState.Success(gitRepos))
    }.catch { e ->
        emit(DataState.dataStateFromError(e))
    }

}