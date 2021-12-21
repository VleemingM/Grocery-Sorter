package nl.vleeming.grocerysorter.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nl.vleeming.grocerysorter.database.dao.GroceryDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideGroceryDao(groceryDatabase: GroceryDatabase): GroceryDao{
        return groceryDatabase.groceryDao()
    }

    @Provides
    @Singleton
    fun provideGroceryDatabase(@ApplicationContext context: Context) : GroceryDatabase{
        return Room.databaseBuilder(context,GroceryDatabase::class.java,"groceryDatabase").build()
    }
}