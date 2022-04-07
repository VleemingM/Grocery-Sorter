package nl.vleeming.grocerysorter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.screen.*
import nl.vleeming.grocerysorter.ui.theme.GrocerySorterTheme
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GrocerySorterTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val groceryList = mutableListOf<GroceryModel>()
    groceryList.add(GroceryModel(product = "banaan"))
    groceryList.add(GroceryModel(product = "Appel"))
    groceryList.add(GroceryModel(product = "Havermout"))
    groceryList.add(GroceryModel(product = "Kattenvoer"))
    groceryList.add(GroceryModel(product = "iets anders"))
    Scaffold(
        content = { GroceryListScreen(groceryList = groceryList, { _, _ -> }, {}) }
    )
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: AddGroceryViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val showAddItemFab = remember {
        mutableStateOf(false)
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showAddItemFab.value = destination.route?.let {
            DrawerScreens.getDrawerScreenForRoute(it)?.shouldShowAddItemFab
        } ?: true
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDrawer = {
        scope.launch {
            drawerState.open()
        }
    }

    val shopList = viewModel.shops.observeAsState(listOf())
    val groceryList = viewModel.groceries.observeAsState(listOf())
    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerScreen(
                onDestinationClicked = { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    TopBar(
                        title = "Groceries",
                        buttonIcon = Icons.Filled.Menu,
                        onButtonClicked = { openDrawer() }
                    )
                }
            },
            floatingActionButton = {
                navController.currentDestination?.route?.let {
                    if (showAddItemFab.value) {
                        AddItemFab(
                            navController = navController,
                        )
                    }
                }

            },
            content = {
                NavHost(
                    navController = navController,
                    startDestination = DrawerScreens.Groceries.route
                ) {
                    composable(DrawerScreens.Groceries.route) {
                        GroceryScreen(
                            shopList = shopList.value,
                            groceryList = groceryList.value,
                            onNavigateToAddProduct = { navController.navigate(DrawerScreens.AddGrocery.route) },
                            onGroceryItemChecked = { groceryModel ->
                                viewModel.deleteGrocery(
                                    groceryModel
                                )
                            })
                    }
                    composable(DrawerScreens.Shop.route) {
                        ShopScreen()
                    }
                    composable(DrawerScreens.AddGrocery.route) {
                        AddGroceryComposable(
                            shopList.value,
                        ) { groceryItem ->
                            viewModel.addGrocery(groceryItem)
                        }
                    }
                    composable(DrawerScreens.AddShop.route) {
                        AddShopComposable()
                    }
                }
            }
        )

    }

}


@Composable
fun AddItemFab(
    navController: NavController
) {
    FloatingActionButton(onClick = {
        if (navController.currentDestination?.route == DrawerScreens.Groceries.route) {
            navController.navigate(DrawerScreens.AddGrocery.route)
        } else if (navController.currentDestination?.route == DrawerScreens.Shop.route) {
            navController.navigate(DrawerScreens.AddShop.route)
        }
    }) { Icon(Icons.Filled.Add, "") }
}

@Composable
fun TopBar(title: String = "", buttonIcon: ImageVector, onButtonClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {
            IconButton(onClick = { onButtonClicked() }) {
                Icon(buttonIcon, contentDescription = "")
            }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant,
    )
}
