package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity

@Entity(
    tableName = "items_with_categories",
    primaryKeys = ["itemId", "categoryId"]
)
data class ItemCategoryCrossRef(
    val itemId: Int,
    val categoryId: Int
)