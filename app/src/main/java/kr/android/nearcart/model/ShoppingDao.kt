package kr.android.nearcart.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    //adding shopping item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(shoppingItem : ShoppingItem)  : Long

    @Update
    suspend fun updateItem(shoppingItem: ShoppingItem) : Int

    @Delete
    suspend fun deleteItem(shoppingItem: ShoppingItem) : Int

//    @Query("select * from `shopping_table`")
//    fun getAllItems() : Flow<List<ShoppingItem>>

    //supports auto-sorting on checked
    @Query("SELECT * FROM shopping_table ORDER BY is_checked ASC, id DESC")
    fun getAllItems(): Flow<List<ShoppingItem>>

    @Query("select * from `shopping_table` where id = :id")
    fun getItemById(id : Long) : Flow<ShoppingItem>
}