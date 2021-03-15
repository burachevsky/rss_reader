package com.burachevsky.rssfeedreader.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.burachevsky.rssfeedreader.data.daos.*
import com.burachevsky.rssfeedreader.data.entities.*
import com.burachevsky.rssfeedreader.utils.Converters

@Database(
    entities = [
        NewsChannelEntity::class,
        NewsItemEntity::class,
        FavoriteItem::class,
        ReadItem::class,
        NewsCategoryEntity::class,
        ItemCategoryCrossRef::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsChannelDao(): NewsChannelDao
    abstract fun newsItemDao(): NewsItemDao
    abstract fun favoriteItemDao(): FavoriteItemDao
    abstract fun readItemDao(): ReadItemDao
    abstract fun newsCategoryDao(): NewsCategoryDao
}
