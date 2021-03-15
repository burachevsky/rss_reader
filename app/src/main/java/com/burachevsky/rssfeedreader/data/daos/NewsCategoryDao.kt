package com.burachevsky.rssfeedreader.data.daos

import androidx.room.*
import com.burachevsky.rssfeedreader.data.entities.ItemCategoryCrossRef
import com.burachevsky.rssfeedreader.data.entities.NewsCategoryEntity

@Dao
interface NewsCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: NewsCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemCategoryCrossRef(itemCategoryCrossRef: ItemCategoryCrossRef)

    @Query("DELETE FROM items_with_categories WHERE itemLink = :itemLink")
    suspend fun deleteItemCategoryCrossRefs(itemLink: String)
}
