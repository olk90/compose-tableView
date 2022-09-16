package de.olk90.tableview.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.olk90.tableview.logic.Address
import de.olk90.tableview.logic.persons


enum class SelectionState(val heading: String) {
    PERSONS("Persons"), ADDRESSES("Addresses")
}

@Composable
inline fun <reified T> TableView(content: MutableState<List<T>>) {
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
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            headerList.forEach {
                Box(modifier = Modifier.clickable { }) {
                    Text(
                        text = it.headerText,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

        // Table contents
        content.value.forEach {
            Row(
                modifier = Modifier.padding(10.dp).fillMaxWidth().clickable { },
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val rowContent = it!!::class.members
                    .filter { f -> f.annotations.any { a -> a is TableHeader } }
                    .sortedBy { k ->
                        val header = getTableHeader(k.annotations)
                        header.columnIndex
                    }
                    .map { t -> t.call(it) }
                rowContent.forEach { rc ->
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

@Composable
fun TableBody(tableState: MutableState<SelectionState>, scroll: ScrollState) {
    Column(Modifier.verticalScroll(scroll)) {
        when (tableState.value) {
            SelectionState.PERSONS -> TableView(mutableStateOf(persons))
            SelectionState.ADDRESSES -> TableView(mutableStateOf(listOf<Address>()))
        }
    }
}