package com.test.imagesearch.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val DATABASE_NAME = "image_content_db"

@Database(
    entities = [ImageContent::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ImageContentTypeConverter::class)
abstract class ImageDatabase : RoomDatabase() {

    companion object {

        private var instance: ImageDatabase? = null

        fun getInstance(context: Context): ImageDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, ImageDatabase::class.java, DATABASE_NAME)
                    .build()
                    .also { instance = it }
            }
        }
    }

    abstract fun imageContentDao(): ImageContentsDao
}