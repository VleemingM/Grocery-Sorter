package nl.vleeming.grocerysorter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.database.repository.GroceryRepository
import nl.vleeming.grocerysorter.database.repository.ShopRepository
import javax.inject.Inject

@HiltViewModel
class AddGroceryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val shopRepository: ShopRepository
) : ViewModel() {

    val shopName = MutableLiveData<String>()
    fun addGrocery(groceryModel: GroceryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.insertGroceries(groceryModel)
        }
    }

    fun getGroceriesForShopId(id: Int): LiveData<List<GroceryModel>> {
        return groceryRepository.getGroceriesForShop(id).asLiveData()
    }

    fun addShop(shopModel: ShopModel) {
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.insertShop(shopModel)
        }
    }

    fun deleteGrocery(groceryModel: GroceryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteGroceries(groceryModel)
        }
    }

    val groceries = groceryRepository.getAllGroceries().asLiveData()
    val shops = shopRepository.getAllShops().asLiveData()

}