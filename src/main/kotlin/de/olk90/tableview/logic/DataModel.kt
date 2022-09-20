package de.olk90.tableview.logic

import de.olk90.tableview.view.TableHeader

data class Person(
    @TableHeader("First Name", 0)
    val firstName: String,
    @TableHeader("Last Name", 1)
    val lastName: String,
    @TableHeader("Age", 2)
    val age: Int,
    @TableHeader("Address", 3)
    val address: Address?
)

data class Address(
    @TableHeader("Country", 0)
    val country: String,
    @TableHeader("State", 1)
    val state: String?,
    @TableHeader("City", 2)
    val city: String,
    @TableHeader("ZIP", 3)
    val zipCode: String,
    @TableHeader("Street", 4)
    val street: String,
    @TableHeader("House No.", 5)
    val houseNumber: Int
) {
    override fun toString(): String {
        return "$street $houseNumber, $zipCode $city"
    }
}