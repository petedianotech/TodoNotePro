package com.todonotepro.app.data

import com.todonotepro.app.native.NativeCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ItemRepository(private val dao: ItemDao) {

    fun getAllItems(): Flow<List<TodoNoteItem>> = dao.getAllItems()

    fun getItemsByType(type: ItemType): Flow<List<TodoNoteItem>> = dao.getItemsByType(type)

    suspend fun getItemById(id: Long): TodoNoteItem? = dao.getItemById(id)

    suspend fun insert(item: TodoNoteItem): Long = dao.insert(item)

    suspend fun update(item: TodoNoteItem) = dao.update(item.copy(updatedAt = System.currentTimeMillis()))

    suspend fun delete(item: TodoNoteItem) = dao.delete(item)

    /**
     * Hybrid search: uses C++ for ultra-fast filtering when list is large,
     * falls back to Room LIKE for simple cases.
     */
    suspend fun searchFast(allItems: List<TodoNoteItem>, query: String): List<TodoNoteItem> =
        withContext(Dispatchers.Default) {
            if (query.isBlank()) return@withContext allItems
            if (allItems.size < 50) {
                // Small list → pure Kotlin is fine
                return@withContext allItems.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.content.contains(query, ignoreCase = true)
                }
            }

            // Large list → native C++ search
            val titles = allItems.map { it.title }.toTypedArray()
            val contents = allItems.map { it.content }.toTypedArray()
            val json = NativeCore.fastSearch(titles, contents, query)
            val indices = JSONArray(json)
            val result = ArrayList<TodoNoteItem>(indices.length())
            for (i in 0 until indices.length()) {
                result.add(allItems[indices.getInt(i)])
            }
            result
        }
}
