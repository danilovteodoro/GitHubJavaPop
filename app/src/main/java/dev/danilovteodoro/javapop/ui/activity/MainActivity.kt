package dev.danilovteodoro.javapop.ui.activity

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.danilovteodoro.javapop.R
import dev.danilovteodoro.javapop.databinding.ActivityMainBinding
import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.ui.adapter.RepositoryAdapter
import dev.danilovteodoro.javapop.ui.viewmodel.GitRepoActions
import dev.danilovteodoro.javapop.ui.viewmodel.GitRepoViewModel
import dev.danilovteodoro.javapop.ui.viewmodel.SearchSatate
import dev.danilovteodoro.javapop.util.AppSuggestionProvider
import util.DataState
import java.lang.StringBuilder
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainAct"
    private var menuItemPesquisar:MenuItem? = null
    private var _binding:ActivityMainBinding? = null
    private val binding:ActivityMainBinding get() = _binding!!
    private val viewModel:GitRepoViewModel by viewModels()
    private lateinit var adapter:RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRcRepositories()
        registerObserver()
        viewModel.callAction(GitRepoActions.GetRepos)
    }

    override fun onNewIntent(intent: Intent) {
        if(intent.action == Intent.ACTION_SEARCH){
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this,
                        AppSuggestionProvider.AUTHORITY,AppSuggestionProvider.MODE)
                        .saveRecentQuery(query,null)
                viewModel.callAction(GitRepoActions.Search(query))
            }
        }
//        menuItemPesquisar?.collapseActionView()
        super.onNewIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        menuItemPesquisar = menu.findItem(R.id.menuPesquisar)
        val searchView = menuItemPesquisar?.actionView as SearchView
        menuItemPesquisar?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.callAction(GitRepoActions.EndSearch)
                return true
            }
        })
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            try {
                setSearchableInfo(searchManager.getSearchableInfo(ComponentName(this@MainActivity,MainActivity::class.java)))
            }catch (e:Exception){
                Log.e(TAG,e.message,e)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupRcRepositories(){
        adapter = RepositoryAdapter(this)
        binding.rcRepositories.layoutManager = LinearLayoutManager(this)
        binding.rcRepositories.adapter = adapter
    }

    private fun registerObserver(){
        viewModel.repositoriesLv.observe(this, Observer { dataState->
            when(dataState){
                is DataState.Loading -> {
                    println("loading")
                }
                is DataState.Success -> {
                    adapter.add(dataState.value)
                }
            }
        })

        viewModel.searchedRepositories.observe(this, Observer { searchState->
            when(searchState){
                is SearchSatate.Done -> {
                    adapter.add(searchState.value)
                }
                is SearchSatate.None ->{
                    viewModel.repositoriesLv.value?.let { dataState ->
                        if(dataState is DataState.Success){
                            adapter.add(dataState.value)
                        }
                    }
                }
            }
        })
    }


}