package de.olk90.tableview.view

import androidx.compose.runtime.MutableState

enum class SelectionState(val heading: String) {
    PERSONS("Persons"), ADDRESSES("Addresses")
}

enum class SortingState {
    ASC, DESC, NONE
}

fun updateSortingStates(
    sortingStates: MutableState<MutableMap<TableHeader, SortingState>>,
    tableHeader: TableHeader
): SortingState {
    val newMap = sortingStates.value
    when (sortingStates.value[tableHeader]!!) {
        SortingState.ASC -> newMap[tableHeader] = SortingState.DESC
        SortingState.DESC -> newMap[tableHeader] = SortingState.NONE
        SortingState.NONE -> newMap[tableHeader] = SortingState.ASC
    }
    sortingStates.value.filter { it.key != tableHeader }.forEach{
        newMap[it.key] = SortingState.NONE
    }

    sortingStates.value = newMap
    return newMap[tableHeader]!!
}


inline fun <reified T : Any> sort(t: T, tableHeader: TableHeader): String {
    val filterCriterion = t::class.members.first { f -> f.annotations.any { a -> a == tableHeader } }
    val call = filterCriterion.call(t)
    return if (call != null) {
        "$call"
    } else {
        ""
    }
}