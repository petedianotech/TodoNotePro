package com.todonotepro.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.todonotepro.app.data.ItemType
import com.todonotepro.app.data.TodoNoteItem
import com.todonotepro.app.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoNoteApp(viewModel: MainViewModel = viewModel()) {
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TodoNote Pro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search todos & notes") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { viewModel.setFilter(null) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = selectedFilter == ItemType.TODO,
                    onClick = { viewModel.setFilter(ItemType.TODO) },
                    label = { Text("Todos") },
                    leadingIcon = if (selectedFilter == ItemType.TODO) {
                        {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
                FilterChip(
                    selected = selectedFilter == ItemType.NOTE,
                    onClick = { viewModel.setFilter(ItemType.NOTE) },
                    label = { Text("Notes") },
                    leadingIcon = if (selectedFilter == ItemType.NOTE) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Note,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) {
                            "No results"
                        } else {
                            "Nothing here yet.\nTap + to add a todo or note."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        ItemCard(
                            item = item,
                            onToggleComplete = { viewModel.toggleComplete(item) },
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddItemDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content, type, priority ->
                viewModel.addItem(title, content, type, priority)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ItemCard(
    item: TodoNoteItem,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.type == ItemType.TODO) {
                IconButton(onClick = onToggleComplete) {
                    Icon(
                        imageVector = if (item.isCompleted) {
                            Icons.Filled.CheckCircle
                        } else {
                            Icons.Outlined.RadioButtonUnchecked
                        },
                        contentDescription = "Toggle complete",
                        tint = if (item.isCompleted) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Note,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (item.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        null
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.content.isNotBlank()) {
                    Text(
                        text = item.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(if (item.type == ItemType.TODO) "Todo" else "Note")
                        },
                        modifier = Modifier.height(24.dp)
                    )
                    if (item.priority > 0) {
                        val label = when (item.priority) {
                            2 -> "High"
                            1 -> "Medium"
                            else -> "Low"
                        }
                        AssistChip(
                            onClick = {},
                            label = { Text(label) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, content: String, type: ItemType, priority: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ItemType.TODO) }
    var priority by remember { mutableIntStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = type == ItemType.TODO,
                        onClick = { type = ItemType.TODO },
                        label = { Text("Todo") }
                    )
                    FilterChip(
                        selected = type == ItemType.NOTE,
                        onClick = { type = ItemType.NOTE },
                        label = { Text("Note") }
                    )
                }
                if (type == ItemType.TODO) {
                    Text("Priority", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(0 to "Low", 1 to "Medium", 2 to "High").forEach { (p, label) ->
                            FilterChip(
                                selected = priority == p,
                                onClick = { priority = p },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), content.trim(), type, priority)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
