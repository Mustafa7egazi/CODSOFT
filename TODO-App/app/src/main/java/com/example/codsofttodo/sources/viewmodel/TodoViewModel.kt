package com.example.codsofttodo.sources.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codsofttodo.pojo.TodoModel
import com.example.codsofttodo.sources.repository.TodoRepo
import com.example.codsofttodo.sources.room.TodoDao
import com.example.codsofttodo.sources.room.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application):AndroidViewModel(application) {

    private val dao:TodoDao
    private val repo:TodoRepo
    val allStoredTodos:LiveData<List<TodoModel>>

    init {
        dao = TodoDatabase.getDatabase(application.applicationContext).dataAccessObject()
        repo = TodoRepo(dao)
        allStoredTodos = repo.allStoredTodos
    }

    fun createNewTodo(todo:TodoModel){
        viewModelScope.launch(Dispatchers.IO){
            repo.createNewTodo(todo)
        }
    }

    fun updateTodo(todo:TodoModel){
        viewModelScope.launch(Dispatchers.IO){
            repo.updateTodo(todo)
        }
    }

    fun deleteTodo(todo:TodoModel){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteTodo(todo)
        }
    }

    fun deleteAllStoredTodo(){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAllStoredTodo()
        }
    }

}