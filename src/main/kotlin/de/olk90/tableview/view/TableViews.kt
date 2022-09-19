package de.olk90.tableview.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
inline fun <reified T : Any> TableView(
    content: MutableState<List<T>>,
    indexColumn: Boolean = false,
    indexColWidth: Dp = 30.dp,
    noinline onRowSelection: () -> Unit
) {
    val fields = T::class.members.filter {
        it.annotations.any { a -> a is TableHeader }
    }.sortedBy {
        val header = getTableHeader(it.annotations)
        header.columnIndex
    }

    var search by remember { mutableStateOf("") }
    val onSearchUpdate: (String) -> Unit = { search = it }

    val tableContent = remember { mutableStateOf(content.value) }
    val onContentUpdate: () -> Unit = {
        val filteredList = content.value.filter {
            val values = fields.map { kc ->
                val value = kc.call(it)
                if (value == null) {
                    ""
                } else {
                    "$value"
                }
            }
            val match = values.any { f -> f.contains(search, true) }
            match
        }
        tableContent.value = filteredList
    }

    Column(
        Modifier.fillMaxWidth()
    ) {

        SearchField(search, onSearchUpdate, onContentUpdate)

        val headerList = fields.flatMap { it.annotations }.filterIsInstance<TableHeader>()
        val fraction = 1.0f / headerList.size

        val stateMap = mutableMapOf<TableHeader, SortingState>()
        headerList.forEach {
            stateMap[it] = SortingState.NONE
        }
        val sortingStates = remember { mutableStateOf(stateMap) }

        val onSortingUpdate: (TableHeader, SortingState) -> Unit = { tableHeader, sortingState ->
            val sortedList: List<T> = when (sortingState) {
                SortingState.ASC -> {
                    tableContent.value.sortedBy { t ->
                        sort(t, tableHeader)
                    }
                }

                SortingState.DESC -> {
                    tableContent.value.sortedByDescending { t ->
                        sort(t, tableHeader)
                    }
                }

                else -> {
                    tableContent.value
                }
            }

            tableContent.value = sortedList

        }

        TableHeader(indexColumn, indexColWidth, headerList, sortingStates, onSortingUpdate, fraction)
        Divider(thickness = 2.dp)
        TableContent(tableContent, indexColumn, indexColWidth, fraction, onRowSelection)

    }
}

@Composable
fun SearchField(
    search: String,
    onSearchUpdate: (String) -> Unit,
    onContentUpdate: () -> Unit
) {
    OutlinedTextField(
        value = search,
        onValueChange = {
            onSearchUpdate(it)
            onContentUpdate()
        },
        label = { Text("Search ...") },
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun TableHeader(
    indexColumn: Boolean,
    indexColWidth: Dp,
    headerList: List<TableHeader>,
    sortingStates: MutableState<MutableMap<TableHeader, SortingState>>,
    onSortingUpdate: (TableHeader, SortingState) -> Unit,
    fraction: Float
) {
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (indexColumn) {
            Box(modifier = Modifier.width(indexColWidth), contentAlignment = Alignment.Center) {
                Text(
                    text = "#",
                    style = MaterialTheme.typography.h6
                )
            }
        }
        headerList.forEach {
            val sortingState = remember { mutableStateOf(SortingState.NONE) }
            Box(
                modifier = Modifier.fillMaxWidth(fraction)
                    .clickable {
                        sortingState.value = updateSortingStates(sortingStates, it)
                        onSortingUpdate(it, sortingState.value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Text(
                        text = it.headerText,
                        style = MaterialTheme.typography.h6
                    )

                    if (sortingState.value == SortingState.DESC) {
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "",
                        )
                    } else if (sortingState.value == SortingState.ASC) {
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T> TableContent(
    tableContent: MutableState<List<T>>,
    indexColumn: Boolean,
    indexColWidth: Dp,
    fraction: Float,
    noinline onRowSelection: () -> Unit
) {
    LazyColumn {
        items(items = tableContent.value) {
            val rowIndex = tableContent.value.indexOf(it) + 1
            TableRow(it, indexColumn, rowIndex, indexColWidth, fraction, onRowSelection)
        }
    }
}

@Composable
inline fun <reified T> TableRow(
    it: T,
    indexColumn: Boolean,
    rowIndex: Int,
    indexColWidth: Dp,
    fraction: Float,
    noinline onRowSelection: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable(onClick = onRowSelection),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (indexColumn) {
            Box(modifier = Modifier.width(indexColWidth), contentAlignment = Alignment.Center) {
                Text("$rowIndex")
            }
        }

        val rowContent = it!!::class.members
            .filter { f -> f.annotations.any { a -> a is TableHeader } }
            .sortedBy { k ->
                val header = getTableHeader(k.annotations)
                header.columnIndex
            }
            .map { t -> t.call(it) }
        rowContent.forEach { rc ->
            Box(modifier = Modifier.fillMaxWidth(fraction), contentAlignment = Alignment.Center) {
                if (rc != null) {
                    Text(text = "$rc")
                } else {
                    Text(text = "--")
                }
            }
        }
    }
}
