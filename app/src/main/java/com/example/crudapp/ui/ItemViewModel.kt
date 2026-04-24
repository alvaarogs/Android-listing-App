package com.example.crudapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.crudapp.data.AppDatabase
import com.example.crudapp.data.Item
import com.example.crudapp.data.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository
    val allItems: LiveData<List<Item>>

    // LiveData para cargar un item por ID en AddEditActivity
    private val _selectedItem = MutableLiveData<Item?>()
    val selectedItem: LiveData<Item?> get() = _selectedItem

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ItemRepository(db.itemDao())
        allItems = repository.allItems.asLiveData()
    }

    fun insert(item: Item) = viewModelScope.launch {
        withContext(Dispatchers.IO) { repository.insert(item) }
    }

    fun update(item: Item) = viewModelScope.launch {
        withContext(Dispatchers.IO) { repository.update(item) }
    }

    fun delete(item: Item) = viewModelScope.launch {
        withContext(Dispatchers.IO) { repository.delete(item) }
    }

    // Ya no es suspend — lanza la coroutine internamente y publica en LiveData
    fun loadItemById(id: Int) = viewModelScope.launch {
        val item = withContext(Dispatchers.IO) { repository.getById(id) }
        _selectedItem.value = item
    }
}