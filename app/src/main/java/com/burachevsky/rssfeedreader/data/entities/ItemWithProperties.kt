package com.burachevsky.rssfeedreader.data.entities

import androidx.room.Embedded
import androidx.room.Junction
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
    val favorite: FavoriteItem?,

    @Relation(
        parentColumn = "itemLink",
        entityColumn = "categoryId",
        associateBy = Junction(ItemCategoryCrossRef::class)
    )
    val categories: List<NewsCategoryEntity>
)
