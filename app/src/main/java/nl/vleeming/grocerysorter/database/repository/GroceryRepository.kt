package nl.vleeming.grocerysorter.database.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.dao.GroceryDao
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import javax.inject.Inject

class GroceryRepository @Inject constructor(private val groceryDao: GroceryDao) {

    suspend fun insertGroceries(groceryModel: GroceryModel) =
        groceryDao.insertGrocery(groceryModel)

    suspend fun updateGroceries(groceryModel: GroceryModel) =
        groceryDao.updateGrocery(groceryModel)

    suspend fun deleteGroceries(groceryModel: GroceryModel) =
        groceryDao.deleteGrocery(groceryModel)

    fun getAllGroceries(): Flow<List<GroceryModel>> = groceryDao.getAllGroceries()

    fun getGroceriesForShop(shopId: Int): Flow<List<GroceryModel>> = groceryDao.getGroceriesForShop(shopId)

}