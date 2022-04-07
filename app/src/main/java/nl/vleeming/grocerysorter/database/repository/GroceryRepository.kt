package nl.vleeming.grocerysorter.database.repository

import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.model.GroceryModel

interface GroceryRepository {
    suspend fun insertGroceries(groceryModel: GroceryModel)
    suspend fun updateGroceries(groceryModel: GroceryModel)
    suspend fun deleteGroceries(groceryModel: GroceryModel)
    fun getAllGroceries(): Flow<List<GroceryModel>>
    fun getGroceriesForShop(shopId: Int): Flow<List<GroceryModel>>
}
