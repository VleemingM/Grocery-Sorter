package nl.vleeming.grocerysorter.database.repository

import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.dao.ShopDao
import nl.vleeming.grocerysorter.database.model.ShopModel
import javax.inject.Inject

open class ShopRepositoryImpl @Inject constructor(private val shopDao: ShopDao) : ShopRepository {
    override suspend fun insertShop(shopModel: ShopModel) =
        shopDao.insertShop(shopModel)

    override suspend fun updateShop(shopModel: ShopModel) =
        shopDao.updateShop(shopModel)

    override suspend fun deleteShop(shopModel: ShopModel) =
        shopDao.deleteShop(shopModel)

    override fun getAllShops(): Flow<List<ShopModel>> = shopDao.getAllShops()
}
