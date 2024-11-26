package cc.reece.cathayhomework_2024.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attraction(
    val id: Int,
    val name: String,
    val introduction: String,
    @SerializedName("open_time")
    val openTime: String,
    val distric: String,
    val address: String,
    val tel: String,
    val email: String,
    val nlat: Double,
    val elong: Double,
    @SerializedName("official_site")
    val officialSite: String,
    val facebook: String,
    val ticket: String,
    val remind: String,
    val modified: String,
    val url: String,
    val category: List<IdName>,
    val target: List<IdName>,
    val service: List<IdName>,
    val friendly: List<IdName>,
    val images: List<Image>,
) : Parcelable

@Parcelize
data class IdName(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class Image(
    val src: String,
    val subject: String,
    val ext: String
) : Parcelable