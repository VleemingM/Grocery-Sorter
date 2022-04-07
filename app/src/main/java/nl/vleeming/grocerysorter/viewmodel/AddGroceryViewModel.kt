package nl.vleeming.grocerysorter.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.database.repository.GroceryRepository
import nl.vleeming.grocerysorter.database.repository.GroceryRepositoryImpl
import nl.vleeming.grocerysorter.database.repository.ShopRepository
import javax.inject.Inject

@HiltViewModel
class AddGroceryViewModel @Inject constructor(
    private val groceryRepositoryImpl: GroceryRepository,
    private val shopRepository: ShopRepository
) : ViewModel() {

    val shopName = MutableLiveData<String>()
    fun addGrocery(groceryModel: GroceryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepositoryImpl.insertGroceries(groceryModel)
        }
    }

    fun getGroceriesForShopId(id: Int): LiveData<List<GroceryModel>> {
        return groceryRepositoryImpl.getGroceriesForShop(id).asLiveData()
    }

    fun addShop(shopModel: ShopModel) {
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.insertShop(shopModel)
        }
    }

    fun deleteGrocery(groceryModel: GroceryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepositoryImpl.deleteGroceries(groceryModel)
        }
    }

    val groceries = groceryRepositoryImpl.getAllGroceries().asLiveData()
    val shops = shopRepository.getAllShops().asLiveData()

}
