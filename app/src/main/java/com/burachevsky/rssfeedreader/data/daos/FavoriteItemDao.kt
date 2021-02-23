package com.burachevsky.rssfeedreader.data.daos

import androidx.room.*
import com.burachevsky.rssfeedreader.data.entities.FavoriteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteItem(item: FavoriteItem)

    @Delete
    suspend fun deleteFavoriteItem(item: FavoriteItem)

    @Query("SELECT EXISTS(SELECT * FROM favorite_items WHERE link = :link LIMIT 1)")
    fun isFavorite(link: String): Boolean

    @Transaction
    @Query("SELECT link FROM favorite_items")
    fun getFavoritesFlow(): Flow<List<String>>
}