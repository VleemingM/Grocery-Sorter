package nl.vleeming.grocerysorter.database.repository

import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.dao.ShopDao
import nl.vleeming.grocerysorter.database.model.ShopModel
import javax.inject.Inject

interface ShopRepository {
    suspend fun insertShop(shopModel: ShopModel)

    suspend fun updateShop(shopModel: ShopModel)

    suspend fun deleteShop(shopModel: ShopModel)

    fun getAllShops(): Flow<List<ShopModel>>
}
