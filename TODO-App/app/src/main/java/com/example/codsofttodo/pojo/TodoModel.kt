package com.example.codsofttodo.pojo

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class TodoModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var title:String,
    var description:String = "empty",
    var priority:Int = 3,
    @ColumnInfo(name = "due_date")
    var dueDate:String,
    @ColumnInfo(name = "completion_status")
    var completionStatus:String = "no"
):Parcelable
