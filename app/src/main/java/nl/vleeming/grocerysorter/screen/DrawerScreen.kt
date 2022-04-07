package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination

sealed class DrawerScreens(val title: String, val route: String) {
    object Groceries : DrawerScreens("Groceries","groceries")
    object AddGrocery : DrawerScreens("Add Grocery","addGrocery")
    object Shop : DrawerScreens("Shop","shop")
    object AddShop : DrawerScreens("Add Shop","addShop")
}

private val screens = listOf(
    DrawerScreens.Groceries,
    DrawerScreens.Shop
)


@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    val scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState())
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerScreen(onDestinationClicked = {})
        }) {

    }
}

@Composable
fun DrawerScreen(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    Text("Groceries", modifier = Modifier.padding(16.dp))
    Divider()

    screens.forEach{ screen->
        Spacer(Modifier.height(24.dp))
        Text(
            text = screen.title,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.clickable {
                onDestinationClicked(screen.route)
            })
    }
}
