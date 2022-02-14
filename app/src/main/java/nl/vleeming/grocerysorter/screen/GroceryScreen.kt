package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import nl.vleeming.grocerysorter.TopBar
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel

@Composable
fun GroceryScreen(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    val list = groceryViewModel.groceries.observeAsState(initial = emptyList())
    GroceryList(groceryList = list.value)
}

@Composable
fun GroceryList(groceryList: List<GroceryModel>) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        groceryList.forEach { grocery ->
            SimpleRow(grocery.product)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryRowPreview() {
    SimpleRow(GroceryModel(product = "DIT IS EEN PRODUCT :-)").product)
}

@Composable
fun SimpleRow(title: String) {
    Column {
        BasicText(
            text = title,
            modifier = Modifier.padding(top = 4.dp)
        )
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun AddGroceryFab(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    FloatingActionButton(onClick = {
        openDialog.value = true
    }) { Icon(Icons.Filled.Add, "") }
    if (openDialog.value) {
        AlertDialog(onDismissRequest = {},
            title = { Text(text = "Dialog title") },
            text =
            { AddGroceryComposable() },
            confirmButton = {
                Button(onClick = {
                    openDialog.value = false
                    groceryViewModel.addGrocery(GroceryModel(product = groceryViewModel.productName.value!!))
                }) { Text(text = "Add grocery") }
            },
            dismissButton = {
                Button(onClick = {
                    openDialog.value = false
                }) { Text(text = "Cancel") }
            })
    }
}


@Composable
fun AddGroceryComposable(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    var text by remember {
        mutableStateOf(TextFieldValue())
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            groceryViewModel.productName.value = it.text
        },
        label = { Text("Enter product") })
    DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {
        ShopList(
            list = listOf(
                ShopModel(shop = "Jumbo"),
                ShopModel(shop = "AH"),
                ShopModel(shop = "Boni")
            )
        )
    }

}

@Composable
fun ShopList(list: List<ShopModel>) {
    list.forEach {
        SimpleRow(title = it.shop)
    }
}