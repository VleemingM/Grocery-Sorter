package nl.vleeming.grocerysorter.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        SimpleRow(title = it.shop, Modifier)
    }
}

@Composable
fun AddShopComposable(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    var text by remember {
        mutableStateOf(TextFieldValue())
    }
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                groceryViewModel.shopName.value = it.text
            },
            label = { Text("Enter shop name") })
        Button(onClick = {
            if (text.text.isNotEmpty()) {
                groceryViewModel.addShop(ShopModel(shop = text.text))
                Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show()
                text = TextFieldValue()
            }
        }) {
            Text(text = "Save")
        }
    }
}
