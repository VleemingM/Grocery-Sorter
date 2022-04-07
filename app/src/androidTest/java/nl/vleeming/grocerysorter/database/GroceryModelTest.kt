package nl.vleeming.grocerysorter.database

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.vleeming.grocerysorter.database.dao.GroceryDao
import nl.vleeming.grocerysorter.database.model.GroceryModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class GroceryModelTest {
    private lateinit var groceryDao: GroceryDao
    private lateinit var db: GroceryDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GroceryDatabase::class.java).build()
        groceryDao = db.groceryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeGroceryAndReadInList() {
        val grocery = GroceryModel(product = "lala")
        runBlocking {
            groceryDao.insertGrocery(grocery)

            val groceryList = groceryDao.getAllGroceries().take(1).toList()
            assertEquals("Grocerylist was not properly inserted", 1, groceryList.size)
        }
    }


}
