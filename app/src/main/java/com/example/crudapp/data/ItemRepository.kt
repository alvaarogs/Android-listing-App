package com.example.crudapp.data

import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun insert(item: Item) = itemDao.insertItem(item)

    suspend fun update(item: Item) = itemDao.updateItem(item)

    suspend fun delete(item: Item) = itemDao.deleteItem(item)

    suspend fun getById(id: Int): Item? = itemDao.getItemById(id)
}