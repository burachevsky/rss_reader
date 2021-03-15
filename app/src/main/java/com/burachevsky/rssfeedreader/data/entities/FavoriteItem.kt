package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = false)
    val favoriteItemId: Int
)
