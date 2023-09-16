package com.example.codsofttodo.adapter


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.codsofttodo.R
import com.example.codsofttodo.databinding.TodoItemLayoutBinding
import com.example.codsofttodo.pojo.TodoModel
import com.example.codsofttodo.sources.viewmodel.TodoViewModel
import com.example.codsofttodo.ui.home.HomeFragmentDirections

class TodosAdapter(private val viewModel: TodoViewModel) :
    ListAdapter<TodoModel, TodosAdapter.ViewHolder>(TodoItemDiff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTodo = getItem(position)
        holder.binding.todoCheckBox.isChecked = currentTodo.completionStatus == "yes"
        holder.binding.todoTitleTV.text = currentTodo.title
         if (currentTodo.completionStatus == "yes")
             holder.binding.todoTitleTV.paintFlags = 
                 holder.binding.todoTitleTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.todoDateTV.text = currentTodo.dueDate
        holder.binding.todoPiriorityTV.text = when (currentTodo.priority) {
            1 -> {
                holder.binding.todoPiriorityTV.setTextColor(holder.binding.root.context.getColor(R.color.myColorRed))
                "Urgent"
            }

            2 -> {
                holder.binding.todoPiriorityTV.setTextColor(holder.binding.root.context.getColor(R.color.myColorOrange))
                "Medium"
            }

            else -> {
                holder.binding.todoPiriorityTV.setTextColor(holder.binding.root.context.getColor(R.color.myColorGreen))
                "Normal"
            }
        }


        holder.binding.todoItemParent.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddTodoFragment(currentTodo)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.todoCheckBox.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                currentTodo.completionStatus = "yes"
            } else {
                currentTodo.completionStatus = "no"
            }
            try {
                viewModel.updateTodo(currentTodo)
                Navigation.findNavController(compoundButton).navigate(R.id.action_homeFragment_self)

            } catch (e: Exception) {
                Toast.makeText(
                    holder.binding.root.context,
                    e.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    inner class ViewHolder(val binding: TodoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class TodoItemDiff : DiffUtil.ItemCallback<TodoModel>() {
        override fun areItemsTheSame(
            oldItem: TodoModel, newItem:
            TodoModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoModel, newItem: TodoModel): Boolean {
            return oldItem == newItem
        }

    }
}