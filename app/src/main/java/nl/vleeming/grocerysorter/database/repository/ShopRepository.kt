package nl.vleeming.grocerysorter.database.repository

import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.dao.ShopDao
import nl.vleeming.grocerysorter.database.model.ShopModel
import javax.inject.Inject

class ShopRepository @Inject constructor(private val shopDao: ShopDao) {
    suspend fun insertShop(shopModel: ShopModel) =
        shopDao.insertShop(shopModel)

    suspend fun updateShop(shopModel: ShopModel) =
        shopDao.updateShop(shopModel)

    suspend fun deleteShop(shopModel: ShopModel) =
        shopDao.deleteShop(shopModel)

    fun getAllShops(): Flow<List<ShopModel>> = shopDao.getAllShops()
}