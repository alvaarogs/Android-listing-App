package com.example.crudapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudapp.R
import com.example.crudapp.adapter.ItemAdapter
import com.example.crudapp.data.Item
import com.example.crudapp.databinding.ActivityListBinding
import com.google.android.material.snackbar.Snackbar

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: ItemViewModel
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Factory explícita igual que en AddEditActivity
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ItemViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Lista de Elementos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupFab()
        observeItems()
    }

    private fun setupRecyclerView() {
        adapter = ItemAdapter(
            onEditClick = { item ->
                val intent = Intent(this, AddEditActivity::class.java)
                intent.putExtra(AddEditActivity.EXTRA_ITEM_ID, item.id)
                startActivity(intent)
            },
            onDeleteClick = { item ->
                showDeleteDialog(item)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAddItem.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
    }

    private fun observeItems() {
        viewModel.allItems.observe(this) { items ->
            adapter.submitList(items)
            if (items.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showDeleteDialog(item: Item) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar elemento")
            .setMessage("¿Estás seguro de que quieres eliminar \"${item.titulo}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.delete(item)
                Snackbar.make(binding.root, "\"${item.titulo}\" eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") { viewModel.insert(item) }
                    .show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { onBackPressedDispatcher.onBackPressed(); true }
            R.id.action_add   -> { startActivity(Intent(this, AddEditActivity::class.java)); true }
            else              -> super.onOptionsItemSelected(item)
        }
    }
}