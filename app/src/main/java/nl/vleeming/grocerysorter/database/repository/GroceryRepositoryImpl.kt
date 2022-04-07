package nl.vleeming.grocerysorter.database.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.dao.GroceryDao
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import javax.inject.Inject

open class GroceryRepositoryImpl @Inject constructor(private val groceryDao: GroceryDao): GroceryRepository {

    override suspend fun insertGroceries(groceryModel: GroceryModel) =
        groceryDao.insertGrocery(groceryModel)

    override suspend fun updateGroceries(groceryModel: GroceryModel) =
        groceryDao.updateGrocery(groceryModel)

    override suspend fun deleteGroceries(groceryModel: GroceryModel) =
        groceryDao.deleteGrocery(groceryModel)

    override fun getAllGroceries(): Flow<List<GroceryModel>> = groceryDao.getAllGroceries()

    override fun getGroceriesForShop(shopId: Int): Flow<List<GroceryModel>> = groceryDao.getGroceriesForShop(shopId)

}
