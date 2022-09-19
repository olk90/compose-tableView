package de.olk90.tableview.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.olk90.tableview.logic.addresses
import de.olk90.tableview.logic.persons
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
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            when (tableState.value) {
                SelectionState.PERSONS -> Text("TODO")
                SelectionState.ADDRESSES -> Text("TODO")
            }
        }
    }
}