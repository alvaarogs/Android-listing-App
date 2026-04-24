package com.example.crudapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapp.data.Item
import com.example.crudapp.databinding.ItemRowBinding

class ItemAdapter(
    private val onEditClick: (Item) -> Unit,
    private val onDeleteClick: (Item) -> Unit
) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.tvTitulo.text = item.titulo
            binding.tvDescripcion.text = item.descripcion.ifEmpty { "Sin descripción" }
            binding.tvTlfn.text = if (item.tlfn.isNotEmpty()) "📞 ${item.tlfn}" else ""
            binding.tvWeb.text = if (item.web.isNotEmpty()) "🌐 ${item.web}" else ""
            binding.tvUbicacion.text = if (item.ubicacion.isNotEmpty()) "📍 ${item.ubicacion}" else ""

            binding.btnEdit.setOnClickListener { onEditClick(item) }
            binding.btnEliminar.setOnClickListener { onDeleteClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }
}