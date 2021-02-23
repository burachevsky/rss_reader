package com.burachevsky.rssfeedreader.data.daos

import androidx.room.*
import com.burachevsky.rssfeedreader.data.entities.ChannelWithItemsWithProperties
import com.burachevsky.rssfeedreader.data.entities.NewsChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsChannelDao {

    @Transaction
    @Query("SELECT * FROM news_channels")
    suspend fun getChannelsWithItems(): List<ChannelWithItemsWithProperties>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(newsChannel: NewsChannelEntity)

    @Delete
    suspend fun deleteChannel(newsChannelEntity: NewsChannelEntity)

    @Transaction
    @Query("SELECT * FROM news_channels")
    fun getChannelsWithItemsFlow(): Flow<List<ChannelWithItemsWithProperties>>
}