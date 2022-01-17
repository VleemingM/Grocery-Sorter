package nl.vleeming.grocerysorter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        content = { GroceryList(groceryList = groceryList) }
    )

}


@Composable
fun MainScreen() {
    val navController = rememberNavController()

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
                        navController.navigate(route){
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = DrawerScreens.Groceries.route
            ) {
                composable(DrawerScreens.Groceries.route) {
                    GroceryScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
                composable(DrawerScreens.Shop.route) {
                    ShopScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
            }
        }
    }
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
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}
