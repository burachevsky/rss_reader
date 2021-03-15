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

    @Query("SELECT EXISTS(SELECT * FROM read_items WHERE readItemId = :itemId LIMIT 1)")
    fun isRead(itemId: Int): Boolean
}
