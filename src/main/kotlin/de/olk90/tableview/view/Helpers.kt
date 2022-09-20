package de.olk90.tableview.view

import androidx.compose.runtime.MutableState

enum class SelectionState(val heading: String) {
    PERSONS("Persons"), ADDRESSES("Addresses")
}

enum class SortingState {
    ASC, DESC, NONE
}

fun updateSortingStates(
    sortingStates: MutableState<Map<TableHeader, MutableState<SortingState>>>,
    tableHeader: TableHeader
) {
    val newMap = sortingStates.value
    when (sortingStates.value[tableHeader]!!.value) {
        SortingState.ASC -> newMap[tableHeader]!!.value = SortingState.DESC
        SortingState.DESC -> newMap[tableHeader]!!.value = SortingState.NONE
        SortingState.NONE -> newMap[tableHeader]!!.value = SortingState.ASC
    }
    sortingStates.value.filter { it.key != tableHeader }.forEach{
        newMap[it.key]!!.value = SortingState.NONE
    }

    sortingStates.value = newMap
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