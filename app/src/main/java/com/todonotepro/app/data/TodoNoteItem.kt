package com.todonotepro.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class TodoNoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String = "",
    val type: ItemType = ItemType.TODO,
    val priority: Int = 1,          // 0=Low, 1=Medium, 2=High
    val dueDate: Long? = null,      // epoch millis
    val isCompleted: Boolean = false,
    val tags: String = "",          // comma-separated for simplicity
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
