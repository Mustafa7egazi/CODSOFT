package com.example.codsofttodo.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.codsofttodo.R
import com.example.codsofttodo.adapter.TodosAdapter
import com.example.codsofttodo.databinding.FragmentHomeBinding
import com.example.codsofttodo.pojo.TodoModel
import com.example.codsofttodo.sources.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: TodoViewModel by lazy {
        ViewModelProvider(this)[TodoViewModel::class.java]
    }

    private lateinit var popupMenu: PopupMenu
    private lateinit var todayTodo: List<TodoModel>
    private lateinit var allTodos: List<TodoModel>

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        val window: Window? = requireActivity().window
        window?.statusBarColor = requireContext().getColor(R.color.myColorOrange)

        viewModel.allStoredTodos.observe(viewLifecycleOwner) { it ->
            binding.loadingTasksIndicator.visibility = View.GONE
            binding.todayTasksNumTV.text = "${it.size} Task"
            if (it.isNotEmpty()) {
                val sortedTodos = it.sortedBy { it.dueDate }
                todayTodo = it.filter { it.dueDate == getCurrentDateFormatted() }
                allTodos = sortedTodos.sortedBy { todo -> todo.completionStatus == "yes" }
                Log.d("7egzz", "onCreateView: $todayTodo")
                val todoAdapter = TodosAdapter(viewModel).apply {
                    submitList(allTodos)
                }
                binding.tasksRecycler.apply {
                    adapter = todoAdapter
                    visibility = View.VISIBLE
                }

            } else {
                todayTodo = emptyList()
                allTodos = emptyList()
                binding.tasksRecycler.visibility = View.GONE
                binding.noTodoTV.visibility = View.VISIBLE
            }
        }


        binding.addingNewTaskFAB.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addTodoFragment)
        }

        binding.popUpMenuTodoFilter.setOnClickListener {
            filterTheTodos()
            popupMenu.show()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun filterTheTodos() {
        popupMenu = PopupMenu(requireContext(), binding.popUpMenuTodoFilter)
        popupMenu.menuInflater.inflate(R.menu.showing_type_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            val adapter = TodosAdapter(viewModel)
            when (menuItem.itemId) {
                R.id.todayTodos -> {
                    binding.todayDateTV.text = "${getCurrentDateFormatted()} : Today"
                    binding.todayTasksNumTV.text = "${todayTodo.size} Task"
                    if (todayTodo.isNotEmpty()) {
                        adapter.submitList(todayTodo)
                        binding.tasksRecycler.apply {
                            this.adapter = adapter
                            visibility = View.VISIBLE
                        }
                        binding.noTodoTV.visibility = View.GONE
                    } else {
                        binding.tasksRecycler.visibility = View.GONE
                        binding.noTodoTV.apply {
                            text = "No tasks for today"
                            visibility = View.VISIBLE
                        }
                    }
                }

                R.id.allTodos -> {
                    binding.todayDateTV.text = "All Tasks"
                    binding.todayTasksNumTV.text = "${allTodos.size} Task"
                    if (allTodos.isNotEmpty()) {
                        adapter.submitList(allTodos)
                        binding.tasksRecycler.apply {
                            this.adapter = adapter
                            visibility = View.VISIBLE
                        }
                        binding.noTodoTV.visibility = View.GONE
                    } else {
                        binding.tasksRecycler.visibility = View.GONE
                        binding.noTodoTV.apply {
                            text = "No tasks, start now!"
                            visibility = View.VISIBLE
                        }
                    }
                }

                else -> {
                    try {
                        viewModel.deleteAllStoredTodo()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            true
        }
    }

    private fun getCurrentDateFormatted(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

}