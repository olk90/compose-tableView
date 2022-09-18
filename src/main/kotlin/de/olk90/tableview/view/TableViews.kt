package de.olk90.tableview.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.olk90.tableview.logic.Address
import de.olk90.tableview.logic.persons


enum class SelectionState(val heading: String) {
    PERSONS("Persons"), ADDRESSES("Addresses")
}

@Composable
inline fun <reified T> TableView(
    content: MutableState<List<T>>,
    indexColumn: Boolean = false,
    indexColWidth: Dp = 30.dp,
    scroll: ScrollState,
    noinline onRowSelection: () -> Unit
) {
    val fields = T::class.members.filter {
        it.annotations.any { a -> a is TableHeader }
    }.sortedBy {
        val header = getTableHeader(it.annotations)
        header.columnIndex
    }

    var searchExpression by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = searchExpression,
            onValueChange = {
                searchExpression = it
            },
            label = { Text("Search ...") },
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            singleLine = true
        )

        // Header row
        val headerList = fields.flatMap { it.annotations }.filterIsInstance<TableHeader>()
        val fraction = 1.0f / headerList.size
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

        // Table contents
        Column(Modifier.verticalScroll(scroll)) {
            content.value.forEach {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clickable(onClick = onRowSelection),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (indexColumn) {
                        Box(modifier = Modifier.width(indexColWidth), contentAlignment = Alignment.Center) {
                            Text("${content.value.indexOf(it) + 1}")
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
        }

    }
}

@Composable
fun TableBody(tableState: MutableState<SelectionState>, scroll: ScrollState) {
    Row {
        Column(Modifier.fillMaxWidth(0.7f)) {
            when (tableState.value) {
                SelectionState.PERSONS -> TableView(mutableStateOf(persons), true, scroll = scroll) { println("Person table selected") }
                SelectionState.ADDRESSES -> TableView(mutableStateOf(listOf<Address>()), scroll = scroll) { println("Addresses table selected") }
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