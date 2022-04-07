package nl.vleeming.grocerysorter

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.database.repository.GroceryRepository
import nl.vleeming.grocerysorter.database.repository.GroceryRepositoryImpl
import nl.vleeming.grocerysorter.database.repository.ShopRepository
import nl.vleeming.grocerysorter.database.repository.ShopRepositoryImpl
import nl.vleeming.grocerysorter.module.RepositoryModule
import nl.vleeming.grocerysorter.screen.AddGroceryComposable
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class AddProductScreenTest {

    lateinit var viewModel: AddGroceryViewModel

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val groceryRepository: GroceryRepository = mock<GroceryRepositoryImpl>()

    @BindValue
    @JvmField
    val shopRepository: ShopRepository = mock<ShopRepositoryImpl>()

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun WhenNoShopsAreProvidedDontShowShopSelector() {
        Mockito.`when`(groceryRepository.getAllGroceries()).thenReturn(flow {
            emit(emptyList())
        })
        Mockito.`when`(shopRepository.getAllShops()).thenReturn(flow {
            emit(emptyList())
        })
        viewModel = AddGroceryViewModel(groceryRepository, shopRepository)

        composeTestRule.setContent {
            AddGroceryComposable(viewModel)
        }
        composeTestRule.onNodeWithTag("ShopPickerTag").assertDoesNotExist()
    }

    @Test
    fun WhenShopsAreAvailableShowShopSelector() {
        Mockito.`when`(groceryRepository.getAllGroceries()).thenReturn(flow {
            emit(emptyList())
        })
        Mockito.`when`(shopRepository.getAllShops()).thenReturn(flow {
            emit(listOf(ShopModel(1, "bol.com")))
        })
        viewModel = AddGroceryViewModel(groceryRepository, shopRepository)
        composeTestRule.setContent {
            AddGroceryComposable(viewModel)
        }
        composeTestRule.onNodeWithTag("ShopPickerTag").assertIsDisplayed()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun WhenSaveIsClickedWithoutANameDontAddToDatabase() {
        Mockito.`when`(groceryRepository.getAllGroceries()).thenReturn(flow {
            emit(emptyList())
        })

        Mockito.`when`(shopRepository.getAllShops()).thenReturn(flow {
            emit(listOf(ShopModel(1, "bol.com")))
        })
        whenever(runBlockingTest { groceryRepository.insertGroceries(any())}).thenThrow(IllegalAccessError())
        viewModel = AddGroceryViewModel(groceryRepository, shopRepository)
        composeTestRule.setContent {
            AddGroceryComposable(viewModel)
        }
        composeTestRule.onNodeWithTag("saveButton").performClick()
    }

    @Test
    fun WhenSaveIsClickedWithOnlyANameForProductSaveToDatabase() {
        Mockito.`when`(groceryRepository.getAllGroceries()).thenReturn(flow {
            emit(emptyList())
        })

        Mockito.`when`(shopRepository.getAllShops()).thenReturn(flow {
            emit(listOf(ShopModel(1, "bol.com")))
        })

        viewModel = AddGroceryViewModel(groceryRepository,shopRepository)
        composeTestRule.setContent {
            AddGroceryComposable(viewModel)
        }
        composeTestRule.onNodeWithTag("productTextField").performTextInput("boter")
        composeTestRule.onNodeWithTag("saveButton").performClick()
        runBlockingTest { verify(groceryRepository, atLeastOnce()).insertGroceries(any())}
    }

}
