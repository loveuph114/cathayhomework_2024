package cc.reece.cathayhomework_2024.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class News(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("posted")
    val postedDate: String,
    @SerializedName("modified")
    val modifiedDate: String,
    val url: String,
) {

    val postedDateOnly: LocalDate
        get() = LocalDateTime.parse(
            postedDate.substring(0, postedDate.indexOf('+')).trim(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ).toLocalDate()
}


