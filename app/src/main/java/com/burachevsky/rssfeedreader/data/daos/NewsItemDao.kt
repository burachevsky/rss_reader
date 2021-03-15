package com.burachevsky.rssfeedreader.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.burachevsky.rssfeedreader.data.entities.NewsItemEntity

@Dao
interface NewsItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItems(items: List<NewsItemEntity>)

    @Query("DELETE FROM news_items")
    suspend fun clear()

    @Query("DELETE FROM news_items WHERE itemChannelId = :feedId")
    suspend fun deleteItemsFromFeed(feedId: Int)
}