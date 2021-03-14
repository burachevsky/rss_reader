package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity

@Entity(
    tableName = "items_with_categories",
    primaryKeys = ["itemLink", "categoryId"]
)
data class ItemCategoryCrossRef(
    val itemLink: String,
    val categoryId: Int
)