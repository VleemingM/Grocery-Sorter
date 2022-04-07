package nl.vleeming.grocerysorter.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import nl.vleeming.grocerysorter.MainActivity
import nl.vleeming.grocerysorter.database.repository.GroceryRepository
import nl.vleeming.grocerysorter.database.repository.GroceryRepositoryImpl
import nl.vleeming.grocerysorter.database.repository.ShopRepository
import nl.vleeming.grocerysorter.database.repository.ShopRepositoryImpl
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGroceryRepository(
        groceryRepositoryImpl: GroceryRepositoryImpl
    ): GroceryRepository

    @Binds
    abstract fun bindShopRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): ShopRepository
}
