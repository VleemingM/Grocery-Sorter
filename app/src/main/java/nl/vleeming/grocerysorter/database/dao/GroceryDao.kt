package nl.vleeming.grocerysorter.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nl.vleeming.grocerysorter.database.model.GroceryModel

@Dao
interface GroceryDao {

    @Insert
    suspend fun insertGrocery(groceryModel: GroceryModel)

    @Update
    suspend fun updateGrocery(groceryModel: GroceryModel)

    @Query("SELECT * FROM grocery_table")
    fun getAllGroceries(): Flow<List<GroceryModel>>

    @Delete
    suspend fun deleteGrocery(groceryModel: GroceryModel)
}