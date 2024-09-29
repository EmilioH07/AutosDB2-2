package com.erha.autosdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.erha.autosdb.application.AutosDBApp
import com.erha.autosdb.data.AutoRepository
import com.erha.autosdb.data.db.model.AutoEntity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import com.erha.autosdb.R
import com.erha.autosdb.databinding.AutoDialogBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class AutoDialog(
    private val newGame: Boolean = true,
    private var game: AutoEntity = AutoEntity(
        marca = "",
        modelo = "",
        colors = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {

    private var _binding: AutoDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: AutoRepository


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AutoDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as AutosDBApp).repository
        builder = AlertDialog.Builder(requireContext())

        // Configurar el spinner de marca
        val marcas = arrayOf("Selecciona una marca","Toyota", "Ford", "Nissan", "Honda", "MG")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, marcas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMarca.adapter = adapter

        // Si es un auto existente, selecciona la marca correcta
        if (!newGame) {
            val position = marcas.indexOf(game.marca)
            if (position >= 0) {
                binding.spinnerMarca.setSelection(position)
            }
        }

        // Configurar otros campos
        binding.apply {
            tietModelo.setText(game.modelo)
            tietColors.setText(game.colors)
        }

        // Construir y devolver el diálogo
        dialog = if (newGame) {
            buildDialog("Guardar", "Cancelar", {
                // Acción de guardar
                binding.apply {
                    game.apply {
                        marca = binding.spinnerMarca.selectedItem.toString()
                        modelo = tietModelo.text.toString()
                        colors = tietColors.text.toString()
                    }
                }

                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.insertAuto(game)
                        }
                        result.await()
                        withContext(Dispatchers.Main) {
                            message("Auto guardado exitosamente")
                            updateUI()
                        }
                    }
                } catch (e: IOException) {
                    message("Error al guardar el auto")
                }
            }, {
                // Acción de cancelar
            })
        } else {
            buildDialog("Actualizar", "Borrar", {
                // Acción de actualizar
                binding.apply {
                    game.apply {
                        marca = binding.spinnerMarca.selectedItem.toString()
                        modelo = tietModelo.text.toString()
                        colors = tietColors.text.toString()
                    }
                }

                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.updateAuto(game)
                        }
                        result.await()
                        withContext(Dispatchers.Main) {
                            message("Auto actualizado exitosamente")
                            updateUI()
                        }
                    }
                } catch (e: IOException) {
                    message("Error al actualizar el auto")
                }
            }, {
                // Acción de borrar
                val context = requireContext()

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm))
                    .setMessage(getString(R.string.confirm_game, game.marca))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        try {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result = async {
                                    repository.deleteAuto(game)
                                }
                                result.await()
                                withContext(Dispatchers.Main) {
                                    message(context.getString(R.string.game_removed))
                                    updateUI()
                                }
                            }
                        } catch (e: IOException) {
                            message("Error al borrar el auto")
                        }
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()
            })
        }

        return dialog
    }

    // Aquí es cuando se destruye
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        // Referencia al AlertDialog para manejar los botones
        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        // Configurar el text watcher
        binding.apply {
            setupTextWatcher(
                tietModelo,
                tietColors
            )
        }
    }

    private fun validateFields(): Boolean =
                binding.tietModelo.text.toString().isNotEmpty() &&
                binding.tietColors.text.toString().isNotEmpty() &&
                        binding.spinnerMarca.selectedItemPosition != 0 &&
                binding.spinnerMarca.selectedItem.toString().isNotEmpty()


    private fun setupTextWatcher(vararg textFields: TextInputEditText) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        }

        textFields.forEach { textField ->
            textField.addTextChangedListener(textWatcher)
        }
    }

    // Función buildDialog
    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog {
        return builder.setView(binding.root)
            .setPositiveButton(btn1Text) { _, _ ->
                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->
                negativeButton()
            }
            .create()
    }
}
