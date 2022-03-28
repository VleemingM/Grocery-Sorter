package nl.vleeming.grocerysorter.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import nl.vleeming.grocerysorter.database.dao.GroceryDao
import nl.vleeming.grocerysorter.database.dao.ShopDao
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel


@Database(
    entities = [GroceryModel::class, ShopModel::class],
    version = 1
)

abstract class GroceryDatabase : RoomDatabase() {

    abstract fun groceryDao(): GroceryDao
    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: GroceryDatabase? = null

        fun getDatabase(context: Context): GroceryDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceryDatabase::class.java,
                    "grocery_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


