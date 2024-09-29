package com.erha.autosdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erha.autosdb.data.db.model.AutoEntity
import com.erha.autosdb.databinding.GameElementBinding

class AutoAdapter(
    private val onGameClicked: (AutoEntity) -> Unit
) : RecyclerView.Adapter<AutoViewHolder>() {

    private var games: MutableList<AutoEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AutoViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: AutoViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)

        // Para manejar el clic en el elemento
        holder.itemView.setOnClickListener {
            onGameClicked(game)
        }
    }

    fun updateList(list: MutableList<AutoEntity>) {
        games = list
        notifyDataSetChanged()
    }
}

