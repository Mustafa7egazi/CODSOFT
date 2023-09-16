package com.example.codsofttodo.sources.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.codsofttodo.pojo.TodoModel
import com.example.codsofttodo.sources.room.TodoDao

class TodoRepo(private val dao: TodoDao) {
    val allStoredTodos = dao.getAllStoredTodo()


    suspend fun createNewTodo(todo: TodoModel){
        dao.createNewTodo(todo)
    }

    suspend fun updateTodo(todo: TodoModel){
        dao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoModel){
        dao.deleteTodo(todo)
    }

    suspend fun deleteAllStoredTodo(){
        dao.deleteAllStoredTodo()
    }
}