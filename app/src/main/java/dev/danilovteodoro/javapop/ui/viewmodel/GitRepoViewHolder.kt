package dev.danilovteodoro.javapop.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.repository.GitRepoRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import util.DataState
import javax.inject.Inject

@HiltViewModel
class GitRepoViewHolder @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: GitRepoRepository
):ViewModel(){
    private var _repositoriesLv:MutableLiveData<DataState<List<Repository>>> = MutableLiveData()
    val repositoriesLv:LiveData<DataState<List<Repository>>> get() = _repositoriesLv

    private fun getGitRepos(){
        repository.getRepositories().onEach { dataState ->
            _repositoriesLv.value = dataState
        }.launchIn(viewModelScope)
    }

    fun callAction(action:GitRepoActions){
        when(action){
            is GitRepoActions.GetRepos ->{
                getGitRepos()
            }
        }
    }

}

sealed class GitRepoActions {
    object GetRepos : GitRepoActions()
}