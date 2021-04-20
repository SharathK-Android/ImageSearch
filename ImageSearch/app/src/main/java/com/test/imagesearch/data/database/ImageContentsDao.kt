package com.test.imagesearch.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageContentsDao {

    /* if this image content already present in database, it will be replaced with new data */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveImageContent(imageContent: ImageContent): Long

    @Query("SELECT * FROM $IMAGE_CONTENT_TABLE WHERE searchQuery=:searchQuery")
    fun getImageContent(searchQuery: String): ImageContent

}