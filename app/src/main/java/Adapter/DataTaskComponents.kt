package Adapter

import java.util.Calendar

data class DataTaskComponents(
    var title: String,
    var text: String,
    var useTime: Boolean = false,
    var alarm: Calendar? = null,
    val codeNotification: Int? = null,
    var check: Boolean = false,
    var checkVisibility: Boolean = false
): java.io.Serializable
