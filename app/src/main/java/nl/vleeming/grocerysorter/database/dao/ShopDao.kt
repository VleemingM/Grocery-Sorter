package nl.vleeming.grocerysorter.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.model.ShopModel

@Dao
interface ShopDao {

    @Insert
    suspend fun insertShop(shopModel: ShopModel)

    @Update
    suspend fun updateShop(shopModel: ShopModel)

    @Query("SELECT * FROM shop_table")
    fun getAllShops(): Flow<List<ShopModel>>

    @Delete
    suspend fun deleteShop(shopModel: ShopModel)
}