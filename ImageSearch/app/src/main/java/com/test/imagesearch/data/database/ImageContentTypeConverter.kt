package com.test.imagesearch.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/* cannot save list of objects directly in room
   so using type converter to convert the list to string and saving it
 */
class ImageContentTypeConverter {
    private var gson = Gson()

    @TypeConverter
    fun stringToThumbnailList(data: String): List<Thumbnail> {
        val listType: Type = object : TypeToken<List<Thumbnail>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun thumbnailListToString(thumbnailList: List<Thumbnail>): String {
        return gson.toJson(thumbnailList)
    }

}