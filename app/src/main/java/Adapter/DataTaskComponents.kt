package Adapter

import java.util.Calendar

data class DataTaskComponents(
    var title: String,
    var text: String,
    var useTime: Boolean = false,
    var alarm: Calendar? = null,
    val codeNotification: Int? = null
): java.io.Serializable
