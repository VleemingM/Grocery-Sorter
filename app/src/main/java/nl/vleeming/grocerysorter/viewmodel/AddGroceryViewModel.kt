package nl.vleeming.grocerysorter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.repository.GroceryRepository
import javax.inject.Inject

@HiltViewModel
class AddGroceryViewModel @Inject constructor(private val groceryRepository: GroceryRepository) : ViewModel() {

    val productName = MutableLiveData<String>()
    fun addGrocery(groceryModel: GroceryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.insertGroceries(groceryModel)
        }
    }
    val groceries = groceryRepository.getAllGroceries().asLiveData()

}