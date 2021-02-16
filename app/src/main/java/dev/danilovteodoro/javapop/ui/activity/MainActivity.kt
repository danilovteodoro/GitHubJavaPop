package dev.danilovteodoro.javapop.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.danilovteodoro.javapop.R
import dev.danilovteodoro.javapop.databinding.ActivityMainBinding
import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.ui.adapter.RepositoryAdapter
import dev.danilovteodoro.javapop.ui.viewmodel.GitRepoActions
import dev.danilovteodoro.javapop.ui.viewmodel.GitRepoViewModel
import util.DataState
import java.lang.StringBuilder
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
    }


}