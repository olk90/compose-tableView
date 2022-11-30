package de.olk90.tableview.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.olk90.tableview.logic.Address
import de.olk90.tableview.logic.Person
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

    val personContent = mutableStateOf(persons)
    val addressContent = mutableStateOf(addresses)

    val person: MutableState<Person?> = remember { mutableStateOf(null) }
    val address: MutableState<Address?> = remember { mutableStateOf(null) }

    val onPersonSelect: (Person) -> Unit = {
        person.value = it
    }

    val onAddressSelect: (Address) -> Unit = {
        address.value = it
    }

    Row {
        Column(Modifier.fillMaxWidth(0.7f)) {
            when (tableState.value) {
                SelectionState.PERSONS -> {
                    TableView(
                        person,
                        personContent,
                        true,
                        onRowSelection = onPersonSelect
                    )
                }

                SelectionState.ADDRESSES -> TableView(
                    address,
                    addressContent,
                    onRowSelection = onAddressSelect
                )
            }
        }
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            when (tableState.value) {
                SelectionState.PERSONS -> PersonView(person)
                SelectionState.ADDRESSES -> AddressView(address)
            }
        }
    }
}

@Composable
fun PersonView(person: MutableState<Person?>) {
    Box(modifier = Modifier.padding(10.dp)) {
        val value = person.value
        if (value == null) {
            Text("Please select person")
        } else {
            val address = value.address
            if (address == null) {
                Text("${value.firstName} ${value.lastName} is ${value.age}")
            } else {
                Text("${value.firstName} ${value.lastName} is ${value.age} and lives in ${value.address.city}, ${value.address.country}")
            }
        }
    }
}

@Composable
fun AddressView(address: MutableState<Address?>) {
    if (address.value == null) {
        Text("Please select address")
    } else {
        Text(address.value.toString())
    }
}