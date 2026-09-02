package com.todonotepro.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.todonotepro.app.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow<ItemType?>(null)
    val selectedFilter: StateFlow<ItemType?> = _selectedFilter.asStateFlow()

    private val allItems: StateFlow<List<TodoNoteItem>>

    val items: StateFlow<List<TodoNoteItem>>

    init {
        val db = AppDatabase.getInstance(application)
        repository = ItemRepository(db.itemDao())

        allItems = repository.getAllItems()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        items = combine(allItems, _searchQuery, _selectedFilter) { list, query, filter ->
            var result = list
            if (filter != null) {
                result = result.filter { it.type == filter }
            }
            if (query.isNotBlank()) {
                // Trigger native search asynchronously when needed
                result // actual filtering done in search flow below for simplicity in v1
            }
            result
        }.flatMapLatest { baseList ->
            if (_searchQuery.value.isBlank()) {
                flowOf(baseList)
            } else {
                flow {
                    emit(repository.searchFast(baseList, _searchQuery.value))
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(type: ItemType?) {
        _selectedFilter.value = type
    }

    fun addItem(title: String, content: String, type: ItemType, priority: Int) {
        viewModelScope.launch {
            repository.insert(
                TodoNoteItem(
                    title = title,
                    content = content,
                    type = type,
                    priority = priority
                )
            )
        }
    }

    fun toggleComplete(item: TodoNoteItem) {
        viewModelScope.launch {
            repository.update(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun deleteItem(item: TodoNoteItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}
