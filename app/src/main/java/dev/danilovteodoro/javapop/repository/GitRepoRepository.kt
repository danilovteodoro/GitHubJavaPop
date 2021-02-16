package dev.danilovteodoro.javapop.repository

import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.network.api.RepositoryApi
import dev.danilovteodoro.javapop.network.mapper.RepositoryNetworkMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import util.DataState
import javax.inject.Inject

class GitRepoRepository @Inject constructor(
    val api: RepositoryApi,
    val networkMapper:RepositoryNetworkMapper
){

    fun getRepositories(): Flow<DataState<List<Repository>>> = flow{
        emit(DataState.Loading)
        val repositoryHolder = api.getRepositories()
        val gitRepos =  networkMapper.mapFromEntityList(repositoryHolder.repositories)
        emit(DataState.Success(gitRepos))
    }.catch { e ->
        emit(DataState.dataStateFromError(e))
    }

}