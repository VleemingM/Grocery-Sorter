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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.screen.*
import nl.vleeming.grocerysorter.ui.theme.GrocerySorterTheme

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
        content = { GroceryListScreen(rememberNavController(),groceryList = groceryList) }
    )

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val openAddItemDialog = remember {
        mutableStateOf(false)
    }
    Surface(color = MaterialTheme.colors.background) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }
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
                    AddItemFab(
                        navController = navController,
                    )
                },
                content = {
                    NavHost(
                        navController = navController,
                        startDestination = DrawerScreens.AddShop.route
                    ) {
                        composable(DrawerScreens.Groceries.route) {
                            GroceryScreen(navController = navController)
                        }
                        composable(DrawerScreens.Shop.route) {
                            ShopScreen()
                        }
                        composable(DrawerScreens.AddGrocery.route) {
                            AddGroceryComposable()
                        }
                        composable(DrawerScreens.AddShop.route) {
                            AddShopComposable()
                        }
                    }
                }
            )

        }
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
