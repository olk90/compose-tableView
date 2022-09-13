package de.olk90.tableview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import de.olk90.tableview.logic.Address
import de.olk90.tableview.logic.Person
import de.olk90.tableview.logic.persons
import de.olk90.tableview.view.TableView

@Composable
fun App() {

    var searchExpression by remember { mutableStateOf("") }

    MaterialTheme {
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
            TableView(mutableStateOf(persons))
            TableView(mutableStateOf(listOf<Address>()))
        }

    }

}

fun main() = application {
    Window(title = "TableView Example", onCloseRequest = ::exitApplication) {
        App()
    }
}
