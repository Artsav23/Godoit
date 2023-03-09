package Adapter

data class TaskComponents(
    var title: String,
    var text: String,
    var useTime: Boolean = false,
    var time: String? = null,
    var date: String? = null
): java.io.Serializable
