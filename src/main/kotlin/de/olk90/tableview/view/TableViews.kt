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

    Column {
        OutlinedTextField(
            value = searchExpression,
            onValueChange = {
                searchExpression = it
            },
            label = { Text("Search ...") },
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            fields.forEach {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val header = getTableHeader(it.annotations)
                    Box(modifier = Modifier.clickable { }) {
                        Text(
                            text = header.headerText,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    content.value.forEach { c ->
                        Box(Modifier.clickable {  }) {
                            val fieldValue = T::class.members.first { f -> f.name == it.name }.call(c)
                            if (fieldValue != null) {
                                Text(text = "$fieldValue")
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
    Column(Modifier.verticalScroll(scroll)) {
        when(tableState.value) {
            SelectionState.PERSONS -> TableView(mutableStateOf(persons))
            SelectionState.ADDRESSES ->  TableView(mutableStateOf(listOf<Address>()))
        }
    }
}