package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    val scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState())
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerScreen()
        }) {

    }
}

@Composable
fun DrawerScreen() {
    Text("Groceries", modifier = Modifier.padding(16.dp))
    Divider()
    Row(Modifier.padding(vertical = 4.dp)) {
        Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "", modifier = Modifier.padding(vertical = 8.dp))
        TextButton(onClick = { /*TODO*/ }) {
            BasicText(text = "Groceries")
        }
    }
    Divider()
    Row(Modifier.padding(vertical = 4.dp)) {
        Icon(imageVector = Icons.Outlined.Place, contentDescription = "",modifier = Modifier.padding(vertical = 8.dp))
        TextButton(onClick = { /*TODO*/ }) {
            BasicText(text = "Shops")
        }
    }
}