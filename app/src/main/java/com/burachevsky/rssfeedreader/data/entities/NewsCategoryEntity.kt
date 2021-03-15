package com.burachevsky.rssfeedreader.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_categories")
data class NewsCategoryEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(index = true)
    val categoryId: Int,
    val name: String
)