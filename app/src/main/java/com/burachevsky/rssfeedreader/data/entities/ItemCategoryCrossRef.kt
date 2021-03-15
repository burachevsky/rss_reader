package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "items_with_categories",
    primaryKeys = ["itemId", "categoryId"],
    indices = [Index("itemId"), Index("categoryId")]
)
data class ItemCategoryCrossRef(
    val itemId: Int,
    val categoryId: Int
)