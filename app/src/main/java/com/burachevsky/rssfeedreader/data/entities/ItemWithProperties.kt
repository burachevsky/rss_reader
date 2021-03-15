package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ItemWithProperties(
    @Embedded
    val item: NewsItemEntity,

    @Relation(
        parentColumn = "itemId",
        entityColumn = "readItemId",
    )
    val read: ReadItem?,

    @Relation(
        parentColumn = "itemId",
        entityColumn = "favoriteItemId",
    )
    val favorite: FavoriteItem?,

    @Relation(
        parentColumn = "itemId",
        entityColumn = "categoryId",
        associateBy = Junction(ItemCategoryCrossRef::class)
    )
    val categories: List<NewsCategoryEntity>
)
