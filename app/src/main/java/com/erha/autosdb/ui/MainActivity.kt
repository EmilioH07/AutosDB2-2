package com.erha.autosdb.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.erha.autosdb.databinding.ActivityMainBinding
import com.erha.autosdb.R
import com.erha.autosdb.application.AutosDBApp
import com.erha.autosdb.data.AutoRepository
import com.erha.autosdb.data.db.AutoDao
import com.erha.autosdb.data.db.model.AutoEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var games: MutableList<AutoEntity> = mutableListOf()
    private lateinit var repository: AutoRepository
    private lateinit var gameAdapter: AutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as AutosDBApp).repository

        gameAdapter = AutoAdapter { selectedGame ->
            // Clic al registro de cada auto
            val dialog = AutoDialog(newGame = false, game = selectedGame, updateUI = {
                updateUI()
            }, message = { text ->
                message(text)
            })
            dialog.show(supportFragmentManager, "dialog2")
        }

        // Establezco el RecyclerView
        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }

        updateUI()
    }

    fun click(view: View) {
        // Manejamos el click del floating action button
        val dialog = AutoDialog(updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(supportFragmentManager, "dialog1")
    }

    private fun message(text: String) {
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.snackbar))
            .show()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            games = repository.getAllAutos()
            binding.tvSinRegistros.visibility =
                if (games.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            gameAdapter.updateList(games)
        }
    }
}