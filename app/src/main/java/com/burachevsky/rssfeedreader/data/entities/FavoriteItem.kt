package com.burachevsky.rssfeedreader.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(index = true)
    val favoriteItemId: Int
)
