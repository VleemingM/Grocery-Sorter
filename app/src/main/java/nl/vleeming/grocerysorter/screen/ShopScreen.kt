package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel


@Composable
fun ShopScreen(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        val list = groceryViewModel.shops.observeAsState(initial = emptyList())
        ShopList(shops = list.value)
    }
}

@Composable
fun ShopList(shops: List<ShopModel>) {
    shops.forEach {
        SimpleRow(title = it.shop)
    }
}

@Composable
fun AddShopComposable(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    var text by remember {
        mutableStateOf(TextFieldValue())
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            groceryViewModel.shopName.value = it.text
        },
        label = { Text("Enter shop name") })
    Button(onClick = {
        groceryViewModel.addShop(ShopModel(shop = text.text))
    }) {
        BasicText(text = "Save")
    }
}