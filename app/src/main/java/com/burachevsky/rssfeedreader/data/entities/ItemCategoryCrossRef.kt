package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "items_with_categories",
    primaryKeys = ["itemId", "categoryId"],
    indices = [Index("itemId")]
)
data class ItemCategoryCrossRef(
    val itemId: Int,
    val categoryId: Int
)