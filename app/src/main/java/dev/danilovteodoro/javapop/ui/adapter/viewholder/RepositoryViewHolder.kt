package dev.danilovteodoro.javapop.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.danilovteodoro.javapop.databinding.InflaterRepositoryBinding

class RepositoryViewHolder(view:View):RecyclerView.ViewHolder(view) {
    val binding:InflaterRepositoryBinding = InflaterRepositoryBinding.bind(view)
}