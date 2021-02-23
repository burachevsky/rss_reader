package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_items")
data class ReadItem(
    @PrimaryKey(autoGenerate = false)
    val link: String
)
