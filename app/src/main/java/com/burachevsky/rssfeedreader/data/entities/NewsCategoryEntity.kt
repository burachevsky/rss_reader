package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_categories")
data class NewsCategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val categoryId: Int,
    val name: String
)