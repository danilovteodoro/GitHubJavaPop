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
class GitRepoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: GitRepoRepository
):ViewModel(){
    private var _repositoriesLv:MutableLiveData<DataState<List<Repository>>> = MutableLiveData()
    val repositoriesLv:LiveData<DataState<List<Repository>>> get() = _repositoriesLv

    private var _searchedRepositories:MutableLiveData<SearchSatate>  = MutableLiveData()
    val searchedRepositories:LiveData<SearchSatate> get() = _searchedRepositories
   

    private fun getGitRepos(){
        repository.getRepositories().onEach { dataState ->
            _repositoriesLv.value = dataState
        }.launchIn(viewModelScope)
    }

    private fun searGitRespos(search:String){
        _repositoriesLv.value?.let { dataState ->
            if (dataState is DataState.Success){
                val filtered = dataState.value.filter { gitHubRepo ->
                    gitHubRepo.name.matches(Regex(search)) ||
                            gitHubRepo.owner.login.matches(Regex(search))
                }
                _searchedRepositories.value = SearchSatate.Done(filtered)
            }
        }
    }

    fun callAction(action:GitRepoActions){
        when(action){
            is GitRepoActions.GetRepos ->{
                getGitRepos()
            }
            is GitRepoActions.Search -> {
                searGitRespos(action.search)
            }
            is GitRepoActions.EndSearch ->{
                _searchedRepositories.value = SearchSatate.None
            }
        }
    }

}

sealed class GitRepoActions {
    object GetRepos : GitRepoActions()
    data class Search(val search:String) : GitRepoActions()
    object EndSearch : GitRepoActions()
}

sealed class SearchSatate {
    object None : SearchSatate()
    data class Done(val value:List<Repository>) : SearchSatate()
}