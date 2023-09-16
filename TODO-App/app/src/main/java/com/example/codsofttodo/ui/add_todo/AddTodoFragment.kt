package com.example.codsofttodo.ui.add_todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.codsofttodo.R
import com.example.codsofttodo.databinding.FragmentAddTodoBinding
import com.example.codsofttodo.pojo.TodoModel
import com.example.codsofttodo.sources.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodoFragment : Fragment() {

    private lateinit var binding: FragmentAddTodoBinding
    private val viewModel: TodoViewModel by lazy {
        ViewModelProvider(this)[TodoViewModel::class.java]
    }
    private val args: AddTodoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_todo, container, false)

        val window: Window? = requireActivity().window
        window?.statusBarColor = requireContext().getColor(R.color.secondaryBlack)

        if (args.todo != null) {
            binding.deleteTodoTV.visibility = View.VISIBLE
            binding.saveEditBtn.visibility = View.VISIBLE
            binding.savingTaskFAB.visibility = View.GONE
            val editableTitle = Editable.Factory.getInstance().newEditable(args.todo!!.title)
            val editableDesc = Editable.Factory.getInstance().newEditable(args.todo!!.description)
            binding.todoTitleET.text = editableTitle
            if (args.todo!!.description != "empty")
                binding.todoDescET.text = editableDesc

            when (args.todo!!.priority) {
                3 -> {
                    binding.normalRB.isChecked = true
                }

                2 -> {
                    binding.mediumRB.isChecked = true
                }

                else -> {
                    binding.urgentRB.isChecked = true
                }
            }
            binding.datePickerTV.text = args.todo!!.dueDate
            binding.completeStatusTV.isChecked = args.todo!!.completionStatus == "yes"



            binding.deleteTodoTV.setOnClickListener {
                try {
                    viewModel.deleteTodo(args.todo!!)
                    showToast("Successfully deleted")
                    findNavController().navigate(R.id.action_addTodoFragment_to_homeFragment)
                } catch (e: Exception) {
                    showToast(e.message.toString())
                }
            }

            binding.saveEditBtn.setOnClickListener {
                val currentTodo = args.todo
                val updatedTodo = validateData()

                if (updatedTodo != null) {
                    currentTodo!!.title = updatedTodo.title
                    currentTodo.description = updatedTodo.description
                    currentTodo.priority = updatedTodo.priority
                    currentTodo.dueDate = updatedTodo.dueDate
                    currentTodo.completionStatus = updatedTodo.completionStatus

                    try {
                        viewModel.updateTodo(currentTodo)
                        showToast("Updated successfully!")
                        findNavController().navigate(R.id.action_addTodoFragment_to_homeFragment)
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                    }

                } else {
                    showToast("Make sure of your data!")
                }
            }
        }


        binding.savingTaskFAB.setOnClickListener {
            val todoItem = validateData()
            if (todoItem != null) {
                try {
                    viewModel.createNewTodo(todoItem)
                    showToast("Successfully saved!")
                    findNavController().navigate(R.id.action_addTodoFragment_to_homeFragment)
                } catch (e: Exception) {
                    showToast(e.message.toString())
                }
            } else {
                showToast("Insert Valid Data")
            }
        }

        binding.arrowBackBtn.setOnClickListener {
            if (checkDataLose()) {
                showDialogForDataLosing()
            } else {
                findNavController().navigate(R.id.action_addTodoFragment_to_homeFragment)
            }
        }

        binding.datePickerTV.setOnClickListener {
            showDatePickerDialog()
        }


        return binding.root
    }

    private fun checkDataLose(): Boolean {
        if (binding.todoTitleET.text.isEmpty() && binding.datePickerTV.text == "Pick date") {
            return false
        }
        return true
    }

    private fun validateData(): TodoModel? {
        val title = binding.todoTitleET.text.toString()
        val description = binding.todoDescET.text.toString()
        val dueDate = binding.datePickerTV.text
        val returnTodo = TodoModel(
            id = 0,
            title = title,
            dueDate = dueDate.toString()
        )
        if (title.isEmpty() || title.isBlank()) {
            showToast("Todo title is required!")
            return null
        } else if (dueDate == "Pick date") {
            showToast("Please select task date")
            return null
        } else {
            val priority = when (binding.priorityRG.checkedRadioButtonId) {
                R.id.normalRB -> 3
                R.id.mediumRB -> 2
                else -> 1
            }
            returnTodo.priority = priority

            if (description.isNotEmpty() && description.isNotBlank()) {
                returnTodo.description = description
            }
            if (binding.completeStatusTV.isChecked) {
                returnTodo.completionStatus = "yes"
            } else {
                returnTodo.completionStatus = "no"
            }
        }

        return returnTodo
    }

    private fun showDialogForDataLosing() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.also {
            it.setTitle("Warning!")
            it.setMessage("You will lose your written data")
            it.setPositiveButton("Discard") { _, _ ->
                findNavController().navigate(R.id.action_addTodoFragment_to_homeFragment)
            }
            it.setNegativeButton("Cancel") { _, _ ->

            }
            it.show()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { datePicker: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Update the Calendar instance with the selected date
                calendar.set(selectedYear, selectedMonth, selectedDay)

                // Format the selected date and set it to the TextView
                val selectedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    .format(calendar.time)

                Log.d("7egzz", "showDatePickerDialog: $selectedDate")

                binding.datePickerTV.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}