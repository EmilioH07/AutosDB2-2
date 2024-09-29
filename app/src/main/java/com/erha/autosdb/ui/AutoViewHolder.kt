package com.erha.autosdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.erha.autosdb.R
import com.erha.autosdb.data.db.model.AutoEntity
import com.erha.autosdb.databinding.GameElementBinding

class AutoViewHolder(
    private val binding: GameElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: AutoEntity) {
        binding.apply {
            tvTitle.text = game.marca
            tvGenre.text = game.modelo
            tvDeveloper.text = game.colors

            // Cargar la imagen según la marca del auto
            val imageResource = when (game.marca) {
                "Toyota" -> R.drawable.toyota // Reemplaza con la imagen correspondiente
                "Ford" -> R.drawable.ford
                "Nissan" -> R.drawable.nissan
                "Honda" -> R.drawable.honda
                "MG" -> R.drawable.mg
                else -> R.drawable.default_image // Imagen por defecto
            }
            // Asignar la imagen al ImageView
            binding.imageView.setImageResource(imageResource)
        }
    }
}

