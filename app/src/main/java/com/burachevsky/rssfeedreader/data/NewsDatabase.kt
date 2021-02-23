package com.burachevsky.rssfeedreader.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.burachevsky.rssfeedreader.data.daos.FavoriteItemDao
import com.burachevsky.rssfeedreader.data.daos.NewsChannelDao
import com.burachevsky.rssfeedreader.data.daos.NewsItemDao
import com.burachevsky.rssfeedreader.data.daos.ReadItemDao
import com.burachevsky.rssfeedreader.data.entities.FavoriteItem
import com.burachevsky.rssfeedreader.data.entities.NewsChannelEntity
import com.burachevsky.rssfeedreader.data.entities.NewsItemEntity
import com.burachevsky.rssfeedreader.data.entities.ReadItem
import com.burachevsky.rssfeedreader.utils.Converters

@Database(
    entities = [
        NewsChannelEntity::class,
        NewsItemEntity::class,
        FavoriteItem::class,
        ReadItem::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsChannelDao(): NewsChannelDao
    abstract fun newsItemDao(): NewsItemDao
    abstract fun favoriteItemDao(): FavoriteItemDao
    abstract fun readItemDao(): ReadItemDao
}
