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
import junit.framework.Assert.assertTrue
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

class ProductListTest {

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()


    @Before
    fun setup() {
    }

    @Test
    fun WhenNoShopsAreAddedDontShowTabLayout() {
        composeTestRule.setContent {
            GroceryScreen(emptyList(), emptyList(), {}, {})
        }

        composeTestRule.onNodeWithTag("shopTabs").assertDoesNotExist()
    }

    @Test
    fun when_more_than_one_shop_is_available_show_shop_tabs() {
        composeTestRule.setContent {
            GroceryScreen(listOf(
                ShopModel(1, "bol"),
                ShopModel(2, "ah")
            ), emptyList(), {}, {})
        }
        composeTestRule.onNodeWithTag("shopTabs").assertExists().assertIsDisplayed()
    }

    @Test
    fun when_clicking_on_got_grocery_checkbox_call_delete_for_grocery() {
        var called = false
        composeTestRule.setContent {
            GroceryScreen(listOf(
                ShopModel(1, "bol"),
            ), listOf(GroceryModel(1,"Brood")), { called = true}, {})
        }
        composeTestRule.onNodeWithTag("groceryCheckBox").performClick()
        assertTrue(called)
    }

    @Test
    fun when_no_items_are_added_show_add_product_helper() {
        composeTestRule.setContent {
            GroceryScreen(
                emptyList(),
                emptyList(), {}, {})
        }

        composeTestRule.onNodeWithTag("addProductPromptScreen").assertIsDisplayed()
    }
}
