package com.burachevsky.rssfeedreader.data.daos

import androidx.room.Dao
import androidx.room.Insert
import com.burachevsky.rssfeedreader.data.entities.ItemCategoryCrossRef
import com.burachevsky.rssfeedreader.data.entities.NewsCategoryEntity

@Dao
interface NewsCategoryDao {

    @Insert
    suspend fun insertCategory(category: NewsCategoryEntity)

    @Insert
    suspend fun insertItemCategoryCrossRef(itemCategoryCrossRef: ItemCategoryCrossRef)
}