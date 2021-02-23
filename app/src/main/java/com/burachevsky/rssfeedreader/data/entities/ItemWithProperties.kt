package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ItemWithProperties(
    @Embedded
    val item: NewsItemEntity,

    @Relation(
        parentColumn = "itemLink",
        entityColumn = "link",
    )
    val read: ReadItem?,

    @Relation(
        parentColumn = "itemLink",
        entityColumn = "link",
    )
    val favorite: FavoriteItem?
)
