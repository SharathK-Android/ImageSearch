package com.test.imagesearch.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

const val IMAGE_CONTENT_TABLE = "image_content"

@Entity(tableName = IMAGE_CONTENT_TABLE)
@TypeConverters(ImageContentTypeConverter::class)
data class ImageContent(
    @PrimaryKey
    val searchQuery: String,
    val thumbnail: List<Thumbnail>
)

@Parcelize
data class Thumbnail(
    val title: String?,
    val imageUrl: String
) : Parcelable
