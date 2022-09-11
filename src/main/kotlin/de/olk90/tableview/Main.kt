package de.olk90.tableview

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun App() {

}

fun main() = application {
    Window(title = "TableView Example", onCloseRequest = ::exitApplication) {
        App()
    }
}
