package de.olk90.tableview

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import de.olk90.tableview.view.TableSelection

@Composable
fun App() {
    MaterialTheme {
        TableSelection()
    }

}

fun main() = application {
    Window(
        title = "TableView Example",
        state = WindowState(size = DpSize(1600.dp, 900.dp)),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
