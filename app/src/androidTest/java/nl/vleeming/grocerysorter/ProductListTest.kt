package nl.vleeming.grocerysorter

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.database.repository.GroceryRepository
import nl.vleeming.grocerysorter.database.repository.GroceryRepositoryImpl
import nl.vleeming.grocerysorter.database.repository.ShopRepository
import nl.vleeming.grocerysorter.database.repository.ShopRepositoryImpl
import nl.vleeming.grocerysorter.module.RepositoryModule
import nl.vleeming.grocerysorter.screen.GroceryScreen
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class ProductListTest {
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
    fun WhenNoShopsAreAddedDontShowTabLayout(){
        whenever(shopRepository.getAllShops()).thenReturn(flow { emit(emptyList()) })
        whenever(groceryRepository.getAllGroceries()).thenReturn(flow { emit(emptyList()) })

        viewModel = AddGroceryViewModel(groceryRepository,shopRepository)
        composeTestRule.setContent {
            GroceryScreen(viewModel, rememberNavController())
        }

        composeTestRule.onNodeWithTag("shopTabs").assertDoesNotExist()
    }

    @Test
    fun when_more_than_one_shop_is_available_show_shop_tabs(){
        whenever(shopRepository.getAllShops()).thenReturn(flow { emit(listOf(ShopModel(1,"bol"),
            ShopModel(2,"ah")
        )) })
        whenever(groceryRepository.getAllGroceries()).thenReturn(flow { emit(emptyList()) })
        whenever(groceryRepository.getGroceriesForShop(any())).thenReturn(flow { emit(emptyList()) })
        viewModel = AddGroceryViewModel(groceryRepository,shopRepository)
        composeTestRule.setContent {
            GroceryScreen(viewModel, rememberNavController())
        }

         composeTestRule.onNodeWithTag("shopTabs").assertExists().assertIsDisplayed()
    }

    @Test
    fun when_clicking_on_got_grocery_checkbox_call_delete_for_grocery(){
        val groceryModel = GroceryModel(1,"brood")
        whenever(shopRepository.getAllShops()).thenReturn(flow { emit(emptyList()) })
        whenever(groceryRepository.getAllGroceries()).thenReturn(flow { emit(
            listOf(groceryModel)
        ) })

        viewModel = AddGroceryViewModel(groceryRepository,shopRepository)
        composeTestRule.setContent {
            GroceryScreen(viewModel, rememberNavController())
        }
        composeTestRule.onNodeWithTag("groceryCheckBox").performClick()

        runBlockingTest { verify(groceryRepository, times(1)).deleteGroceries(groceryModel)}
    }

    @Test
    fun when_no_items_are_added_show_add_product_helper(){
        whenever(shopRepository.getAllShops()).thenReturn(flow { emit(emptyList()) })
        whenever(groceryRepository.getAllGroceries()).thenReturn(flow { emit(emptyList()) })
        viewModel = AddGroceryViewModel(groceryRepository,shopRepository)

        composeTestRule.setContent {
            GroceryScreen(viewModel, rememberNavController())
        }

        composeTestRule.onNodeWithTag("addProductPromptScreen").assertIsDisplayed()
    }
}
