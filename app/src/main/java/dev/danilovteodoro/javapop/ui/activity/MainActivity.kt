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
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appbar)
        title = ""
        setupRcRepositories()
        setupDrawerToogle()
        registerObserver()
        binding.layoutError.btnRetry.setOnClickListener {
            hideError()
            viewModel.callAction(GitRepoActions.GetRepos)
        }
        if (viewModel.repositoriesLv.value == null){

            viewModel.callAction(GitRepoActions.GetRepos)
        }
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

    fun setupDrawerToogle(){
        drawerToggle = object : ActionBarDrawerToggle(this,binding.drawerLayout,
        binding.appbar,R.string.str_open,R.string.str_close){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }
        }
        binding.drawerLayout.post {
            drawerToggle.syncState()
        }

        binding.mainNavigation.setNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.menuSortByRepoName -> viewModel.callAction(GitRepoActions.SortByName)
                R.id.menuSortByRepoStar -> viewModel.callAction(GitRepoActions.SortByStar)
                R.id.menuSortByRepoFork -> viewModel.callAction(GitRepoActions.SortByFork)
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun registerObserver(){
        viewModel.repositoriesLv.observe(this, Observer { dataState->
            when(dataState){
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    hideProgress()
                    adapter.add(dataState.value)
                }
                is DataState.ServerError ->{
                    hideProgress()
                    showError(getString(R.string.error_server),R.drawable.ic_error_outline)
                }
                is DataState.TimeoutError ->{
                    hideProgress()
                    showError(getString(R.string.error_timeout),R.drawable.ic_error_outline)
                }
                is DataState.Error -> {
                    hideProgress()
                    showError(getString(R.string.error_generic),R.drawable.ic_error_outline)
                }
                is DataState.NoConnection ->{
                    hideProgress()
                    showError(getString(R.string.error_network),R.drawable.ic_network)
                    Snackbar.make(binding.root,R.string.error_network,Snackbar.LENGTH_SHORT)
                            .show()
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

    private fun showProgress(){
        binding.rcRepositories.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress(){
        binding.rcRepositories.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
    }

    private fun showError(errorMessage:String,icon:Int){
        showError()
        binding.layoutError.txtErrorMessage.text = errorMessage
        binding.layoutError.imgError.setImageResource(icon)
    }
    private fun showError(){
        binding.layoutError.main.visibility = View.VISIBLE
        binding.rcRepositories.visibility = View.GONE
    }

    private fun hideError(){
        binding.layoutError.main.visibility = View.GONE
        binding.rcRepositories.visibility = View.VISIBLE
    }




}