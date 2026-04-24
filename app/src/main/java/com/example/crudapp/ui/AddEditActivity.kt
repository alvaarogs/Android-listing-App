package com.example.crudapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.crudapp.R
import com.example.crudapp.data.Item
import com.example.crudapp.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var viewModel: ItemViewModel
    private var existingItem: Item? = null
    private var itemId: Int = -1

    companion object {
        const val EXTRA_ITEM_ID = "extra_item_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ItemViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemId = intent.getIntExtra(EXTRA_ITEM_ID, -1)

        if (itemId != -1) {
            supportActionBar?.title = "✏️ Editar Elemento"
            viewModel.selectedItem.observe(this) { item ->
                item?.let {
                    existingItem = it
                    binding.etTitulo.setText(it.titulo)
                    binding.etDescription.setText(it.descripcion)
                    binding.etPhone.setText(it.tlfn)
                    binding.etWebsite.setText(it.web)
                    binding.etUbicacion.setText(it.ubicacion)
                }
            }
            viewModel.loadItemById(itemId)
        } else {
            supportActionBar?.title = "➕ Nuevo Elemento"
        }

        binding.btnGuardar.setOnClickListener { saveItem() }
    }

    private fun saveItem() {
        val titulo     = binding.etTitulo.text.toString().trim()
        val descripcion = binding.etDescription.text.toString().trim()
        val tlfn       = binding.etPhone.text.toString().trim()
        val web        = binding.etWebsite.text.toString().trim()
        val ubicacion  = binding.etUbicacion.text.toString().trim()

        if (titulo.isEmpty()) {
            binding.tilTitulo.error = "El título es obligatorio"
            return
        }
        binding.tilTitulo.error = null

        val item = Item(
            id          = existingItem?.id ?: 0,
            titulo      = titulo,
            descripcion = descripcion,
            tlfn        = tlfn,
            web         = web,
            ubicacion   = ubicacion
        )

        if (existingItem != null) {
            viewModel.update(item)
            Toast.makeText(this, "Elemento actualizado ✅", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insert(item)
            Toast.makeText(this, "Elemento creado ✅", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_save  -> { saveItem(); true }
            else              -> super.onOptionsItemSelected(item)
        }
    }
}