package Adapter

data class TaskComponents(
    var title: String,
    var text: String?,
    var useTime: Boolean = false,
    var time: String
)
