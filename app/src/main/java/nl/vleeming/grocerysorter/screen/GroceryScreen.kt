package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.LinearScale
import androidx.compose.material.icons.sharp.AdUnits
import androidx.compose.material.icons.sharp.LinearScale
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GroceryScreen(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    val shopList = groceryViewModel.shops.observeAsState(initial = emptyList())
    val list = groceryViewModel.groceries.observeAsState(initial = emptyList())
    if (shopList.value.size > 1) {
        //Add one for the "All" shop
        val shopTabState = rememberPagerState()
        Column {
            ShowShopTabs(shopTabState = shopTabState, shopList.value)
            HorizontalPager(
                count = shopList.value.count(),
                state = shopTabState,
                modifier = Modifier.fillMaxHeight(1f),
                verticalAlignment = Alignment.Top
            ) { position ->
                val shoppingListForShop = groceryViewModel.getGroceriesForShopId(
                    shopList.value[position].id!!
                ).observeAsState(initial = emptyList())
                GroceryList(
                    groceryList = shoppingListForShop.value
                ) { checked, groceryModel ->
                    if (checked) {
                        groceryViewModel.deleteGrocery(groceryModel)
                    }
                }
            }
        }
    } else {
        GroceryList(groceryList = list.value)
    }
}

@Composable
fun GroceryList(
    groceryList: List<GroceryModel>,
    onCheckboxChecked: ((Boolean, GroceryModel) -> Unit)? = null
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        groceryList.forEach { grocery ->
            GroceryItemRow(grocery, onCheckboxChecked)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroceryRowPreview() {
    GroceryItemRow(GroceryModel(product = "DIT IS EEN PRODUCT :-)"))
}

@Composable
fun GroceryItemRow(
    groceryModel: GroceryModel,
    onCheckboxChecked: ((Boolean, GroceryModel) -> Unit)? = null
) {
    Column {
        Row(
            modifier = Modifier.padding(top = 4.dp, end = 8.dp, start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = false, onCheckedChange = {
                onCheckboxChecked?.invoke(it, groceryModel)
            })
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = groceryModel.product
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        //Eventually fix this icon
                        imageVector = Icons.Sharp.LinearScale, contentDescription = "Weight icon",
                    )
                    Text(text = "100")
                }
            }
        }
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun SimpleRow(title: String) {
    Column {
        Text(
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
@Preview
fun AddGroceryComposablePreview() {
    AddGroceryComposable()
}

@Composable
fun AddGroceryComposable(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf(TextFieldValue())
    }
    var selectedIndex by remember { mutableStateOf(0) }
    val shopList = groceryViewModel.shops.observeAsState(initial = emptyList())
    Column {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Enter product") })
        if (shopList.value.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Row(
                    modifier = Modifier
                        .clickable(onClick = { expanded = true })
                ) {
                    Text(
                        shopList.value[selectedIndex].shop
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false })
                {
                    shopList.value.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                        ) {
                            SimpleRow(title = s.shop)
                        }
                    }
                }
            }
        }
        Button(onClick = {
            val shop = shopList.value.getOrNull(selectedIndex)
            val item = GroceryModel(
                product = text.text,
                shop = shop?.id
            )
            groceryViewModel.addGrocery(item)
        }) {
            BasicText(text = "Save")
        }
    }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowShopTabs(shopTabState: PagerState, shops: List<ShopModel>) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = shopTabState.currentPage,
        contentColor = Color.White,
    ) {
        shops.forEachIndexed { index, shopModel ->
            Tab(
                text = { Text(shopModel.shop) },
                selected = shopTabState.currentPage == index,
                onClick = {
                    scope.launch {
                        shopTabState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun ShowShopTabsPreview() {
    val shopTabState = rememberPagerState()
    ShowShopTabs(
        shopTabState = shopTabState, shops = listOf(
            ShopModel(shop = "AH"),
            ShopModel(shop = "AH"),
            ShopModel(shop = "AH"),
            ShopModel(shop = "AH"),
            ShopModel(shop = "AH"),
            ShopModel(shop = "AH"),
        )
    )
}

