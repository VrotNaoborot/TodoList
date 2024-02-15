package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.todolist.databinding.TodoItemBinding

class TodoListAdapter(private val context: Context) : BaseAdapter() {
    private val plantList = ArrayList<TodoItem>()
    private var onItemClickListener: ((TodoItem) -> Unit)? = null

    fun updateItem(position: Int, updatedTodoItem: TodoItem) {
        if (position in 0 until plantList.size) {
            plantList[position] = updatedTodoItem
            notifyDataSetChanged() // Обновление списка после изменения элемента
        }
    }

    fun getPosition(item: TodoItem): Int {
        return plantList.indexOf(item)
    }

    override fun getCount(): Int {
        return plantList.size
    }

    override fun getItem(position: Int): TodoItem {
        return plantList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        holder.bind(item)

        // Установка слушателя кликов
        view.setOnClickListener {
            onItemClickListener?.invoke(item)
        }

        return view
    }

    fun addItem(item: TodoItem) {
        plantList.add(0, item)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (TodoItem) -> Unit) {
        onItemClickListener = listener
    }

    private class ViewHolder(itemView: View) {
        private val binding = TodoItemBinding.bind(itemView)

        fun bind(item: TodoItem) {
            binding.headerList.text = item.header
            binding.previewText.text = item.previewText
            binding.tvDateChange.text = item.dateChange
        }
    }
}
