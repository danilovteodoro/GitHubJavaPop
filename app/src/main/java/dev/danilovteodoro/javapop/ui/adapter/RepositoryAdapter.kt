package dev.danilovteodoro.javapop.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.danilovteodoro.javapop.R
import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.ui.adapter.viewholder.RepositoryViewHolder

class RepositoryAdapter(context: Context):RecyclerView.Adapter<RepositoryViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var repos:List<Repository> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = inflater.inflate(R.layout.inflater_repository,parent,false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val current = repos[position]
        holder.binding.txtName.text = current.name
        holder.binding.txtDescription.text = current.description
        holder.binding.txtUsername.text = current.owner.login
        Picasso.get().load(current.owner.avatarUrl)
            .into(holder.binding.imgAvatar)
    }

    override fun getItemCount(): Int {
        return repos.count()
    }

     fun add(repos:List<Repository>){
        this.repos = repos
        notifyDataSetChanged()
    }
}