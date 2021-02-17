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

    private fun sort(sortBy: SortBy){
        _repositoriesLv.value?.let { dataState ->
            if (dataState is DataState.Success){
                 val sorted = when(sortBy){
                     SortBy.NAME -> dataState.value.sortedBy { gitHubRepo -> gitHubRepo.name }
                     SortBy.STAR -> dataState.value.sortedBy { gitHubRepo -> gitHubRepo.starCount }
                     else -> dataState.value.sortedBy { gitHubRepo -> gitHubRepo.forksCount }
                 }
                _repositoriesLv.value = DataState.Success(sorted)
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
            is GitRepoActions.SortByName -> {
                sort(SortBy.NAME)
            }
            is GitRepoActions.SortByStar -> {
                sort(SortBy.STAR)
            }
            is GitRepoActions.SortByFork -> {
                sort(SortBy.FORK)
            }
        }
    }

}

sealed class GitRepoActions {
    object GetRepos : GitRepoActions()
    data class Search(val search:String) : GitRepoActions()
    object EndSearch : GitRepoActions()
    object SortByName : GitRepoActions()
    object SortByStar : GitRepoActions()
    object SortByFork : GitRepoActions()
}

sealed class SearchSatate {
    object None : SearchSatate()
    data class Done(val value:List<Repository>) : SearchSatate()
}

private enum class SortBy {
    NAME,STAR,FORK
}