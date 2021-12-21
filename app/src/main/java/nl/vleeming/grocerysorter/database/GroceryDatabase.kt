package nl.vleeming.grocerysorter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nl.vleeming.grocerysorter.database.dao.GroceryDao
import nl.vleeming.grocerysorter.database.model.GroceryModel

@Database(
    entities = [GroceryModel::class],
    version = 1
)
abstract class GroceryDatabase : RoomDatabase() {

    abstract fun groceryDao(): GroceryDao

    companion object{
        @Volatile
        private var INSTANCE: GroceryDatabase? = null

        fun getDatabase(context: Context): GroceryDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    GroceryDatabase::class.java,
                "grocery_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}