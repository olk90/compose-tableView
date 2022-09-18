package de.olk90.tableview.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import kotlinx.coroutines.runBlocking

@Composable
fun TableSelection() {
    val scroll = rememberScrollState()
    val tableState = remember { mutableStateOf(SelectionState.PERSONS) }

    Column {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "TableSelection") },
                    actions = {

                    }
                )
            },
            content = {
                Column {
                    Tabs(tableState, scroll)
                    TableBody(tableState)
                }
            }
        )
    }
}

@Composable
fun Tabs(tableState: MutableState<SelectionState>, scroll: ScrollState) {
    TabRow(selectedTabIndex = 0) {
        SelectionState.values().forEach {
            Tab(text = { Text(it.heading) }, selected = tableState.value == it, onClick = {
                tableState.value = it
                runBlocking {
                    scroll.scrollTo(0)
                }
            })
        }
    }
}
