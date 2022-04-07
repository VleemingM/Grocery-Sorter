package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nl.vleeming.grocerysorter.ui.theme.GrocerySorterTheme

@Composable
fun AddProductPrompt(navController: NavController) {
    Column(modifier = Modifier.testTag("addProductPromptScreen").fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.AddShoppingCart, contentDescription = "Add shopping cart")
        Text( "There are no items in your list")
        TextButton(onClick = { navController.navigate(DrawerScreens.AddGrocery.route) }) {
            Text(text = "Add items")
        }
        
    }
}

@Preview(showBackground = true)
@Composable
fun AddProductPromptPreview() {
    val navController = rememberNavController()
    GrocerySorterTheme {
        AddProductPrompt(navController)
    }
    
}
