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
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import nl.vleeming.grocerysorter.database.model.GroceryModel
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


class AddProductScreenTest {

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){
    }

    @Test
    fun WhenNoShopsAreProvidedDontShowShopSelector() {
        composeTestRule.setContent {
            AddGroceryComposable(emptyList(),{})
        }
        composeTestRule.onNodeWithTag("ShopPickerTag").assertDoesNotExist()
    }

    @Test
    fun when_shops_are_available_show_show_selector() {
        val addItem : (GroceryModel) -> Unit = mock()
        composeTestRule.setContent {
            AddGroceryComposable(listOf(ShopModel(1, "bol.com")), addItem)
        }
        composeTestRule.onNodeWithTag("ShopPickerTag").assertIsDisplayed()
    }


    @Test
    fun when_save_is_clicked_without_text_dont_call_add_item(){
        var called = false

        composeTestRule.setContent {
            AddGroceryComposable(emptyList()) { called = true }
        }
        composeTestRule.onNodeWithTag("saveButton").performClick()
        assertFalse(called)
    }

    @Test
    fun WhenSaveIsClickedWithOnlyANameForProductCallAddItem() {
        val addItem : (GroceryModel) -> Unit = mock()

        composeTestRule.setContent {
            AddGroceryComposable(emptyList(), addItem)
        }
        composeTestRule.onNodeWithTag("productTextField").performTextInput("boter")
        composeTestRule.onNodeWithTag("saveButton").performClick()
        verify(addItem, atLeastOnce())
    }

}
