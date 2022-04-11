package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.LinearScale
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
fun GroceryScreen(
    shopList: List<ShopModel>,
    groceryList: List<GroceryModel>,
    onGroceryItemChecked: (GroceryModel) -> Unit,
    onNavigateToAddProduct: () -> Unit
) {
    if (shopList.size > 1) {
        //Add one for the "All" shop
        val shopTabState = rememberPagerState()
        Column {
            ShowShopTabs(shopTabState = shopTabState, shopList)
            HorizontalPager(
                count = shopList.count(),
                state = shopTabState,
                modifier = Modifier.fillMaxHeight(1f),
                verticalAlignment = Alignment.Top
            ) { position ->
                val shoppingListForShop =
                    groceryList.filter {
                        val shopId =shopList[position].id
                        (it.shop?.equals(shopId) ?: false) || it.id == null
                    }

                GroceryListScreen(
                    groceryList = shoppingListForShop, { checked, groceryModel ->
                        if (checked) {
                            onGroceryItemChecked(groceryModel)
                        }
                    }) {
                    onNavigateToAddProduct()
                }
            }
        }
    } else {
        GroceryListScreen(
            groceryList = groceryList,
            { checked, groceryModel ->
                if (checked) {
                    onGroceryItemChecked(groceryModel)
                }
            }) {
            onNavigateToAddProduct()
        }
    }
}

@Composable
fun GroceryListScreen(
    groceryList: List<GroceryModel>,
    onCheckboxChecked: ((Boolean, GroceryModel) -> Unit),
    onNavigateToAddProduct: () -> Unit
) {
    if (groceryList.isNotEmpty()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            groceryList.forEach { grocery ->
                GroceryItemRow(grocery, onCheckboxChecked)
            }
        }
    } else {
        AddProductPrompt(onNavigateToAddProduct)
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
            }, modifier = Modifier.testTag("groceryCheckBox"))
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
fun SimpleRow(title: String, modifier: Modifier) {
    Column(modifier = modifier) {
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


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowShopTabs(shopTabState: PagerState, shops: List<ShopModel>) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = shopTabState.currentPage,
        contentColor = Color.White,
        modifier = Modifier.testTag("shopTabs")
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
