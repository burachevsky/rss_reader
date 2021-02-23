package com.burachevsky.rssfeedreader.data.daos

import androidx.room.*
import com.burachevsky.rssfeedreader.data.entities.ReadItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReadItem(item: ReadItem)

    @Delete
    suspend fun deleteReadItem(item: ReadItem)

    @Query("SELECT EXISTS(SELECT * FROM read_items WHERE link = :link LIMIT 1)")
    fun isRead(link: String): Boolean

    @Transaction
    @Query("SELECT link FROM read_items")
    fun getReadItemsFlow(): Flow<List<String>>
}
