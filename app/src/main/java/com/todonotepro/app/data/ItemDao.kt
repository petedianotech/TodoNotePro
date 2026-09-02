package com.todonotepro.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY isCompleted ASC, priority DESC, updatedAt DESC")
    fun getAllItems(): Flow<List<TodoNoteItem>>

    @Query("SELECT * FROM items WHERE type = :type ORDER BY isCompleted ASC, priority DESC, updatedAt DESC")
    fun getItemsByType(type: ItemType): Flow<List<TodoNoteItem>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Long): TodoNoteItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoNoteItem): Long

    @Update
    suspend fun update(item: TodoNoteItem)

    @Delete
    suspend fun delete(item: TodoNoteItem)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM items WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<TodoNoteItem>>
}
