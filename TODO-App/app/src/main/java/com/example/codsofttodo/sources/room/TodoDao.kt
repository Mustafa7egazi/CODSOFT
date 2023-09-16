package com.example.codsofttodo.sources.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.codsofttodo.pojo.TodoModel

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewTodo(todo: TodoModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTodo(todo: TodoModel)

    @Delete
    suspend fun deleteTodo(todo: TodoModel)

    @Query("SELECT * FROM todo_table ORDER BY priority ASC")
    fun getAllStoredTodo(): LiveData<List<TodoModel>>

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllStoredTodo()

}