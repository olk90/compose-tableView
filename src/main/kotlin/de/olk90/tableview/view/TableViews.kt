package de.olk90.tableview.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
inline fun <reified T> TableView(content: MutableState<List<T>>) {
    val fields = T::class.members.filter {
        it.annotations.any { a -> a is TableHeader }
    }.sortedBy {
        val header = getTableHeader(it.annotations)
        header.columnIndex
    }
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        fields.forEach {
            Column(verticalArrangement = Arrangement.Center) {
                val header = getTableHeader(it.annotations)
                Box(modifier = Modifier.clickable { }) {
                    Text(
                        text = header.headerText,
                        style = MaterialTheme.typography.h6
                    )
                }

                content.value.forEach { c ->
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