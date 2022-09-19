package de.olk90.tableview.view

@Target(AnnotationTarget.PROPERTY)
annotation class TableHeader(
    val headerText: String,
    val columnIndex: Int
)


fun getTableHeader(annotations: List<Annotation>): TableHeader {
    val header = annotations.firstOrNull { a -> a is TableHeader }
        ?: throw IllegalArgumentException("Given annotations don't contain a TableHeader")
    return header as TableHeader
}
