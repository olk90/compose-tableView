package de.olk90.tableview.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.olk90.tableview.logic.addresses
import de.olk90.tableview.logic.persons


enum class SelectionState(val heading: String) {
    PERSONS("Persons"), ADDRESSES("Addresses")
}

@Composable
inline fun <reified T> TableView(
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

        TableHeader(indexColumn, indexColWidth, headerList, fraction)
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
            Box(
                modifier = Modifier.fillMaxWidth(fraction)
                    .clickable {
                        // TODO sort entries
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.headerText,
                    style = MaterialTheme.typography.h6
                )
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

@Composable
fun TableBody(tableState: MutableState<SelectionState>) {
    Row {
        Column(Modifier.fillMaxWidth(0.7f)) {
            when (tableState.value) {
                SelectionState.PERSONS -> TableView(
                    mutableStateOf(persons),
                    true
                ) { println("Person table selected") }

                SelectionState.ADDRESSES -> TableView(
                    mutableStateOf(addresses)
                ) { println("Addresses table selected") }
            }
        }
        Column(Modifier.fillMaxWidth()) {
            when (tableState.value) {
                SelectionState.PERSONS -> Text("TODO")
                SelectionState.ADDRESSES -> Text("TODO")
            }
        }
    }
}