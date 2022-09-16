package de.olk90.tableview

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import de.olk90.tableview.view.TableSelection

@Composable
fun App() {
    MaterialTheme {
        TableSelection()
    }

}

fun main() = application {
    Window(title = "TableView Example", onCloseRequest = ::exitApplication) {
        App()
    }
}
