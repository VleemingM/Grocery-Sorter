package nl.vleeming.grocerysorter.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.database.model.ShopModel

@Composable
@Preview
fun AddGroceryComposablePreview() {
    AddGroceryComposable(
        listOf(ShopModel(1,"bol.com")),
        {})
}


@Composable
fun AddGroceryComposable(
    shopList: List<ShopModel>,
    onAddGrocery: (GroceryModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf(TextFieldValue())
    }
    var textError by remember {
        mutableStateOf(false)
    }
    var selectedIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = {
                if (it.text.isNotEmpty()) {
                    textError = false
                }
                text = it
            },
            label = { Text("Enter product") },
            isError = textError,
            modifier = Modifier.testTag("productTextField")
        )

        if (shopList.isNotEmpty()) {
            ShopPickerComposable(
                shopList = shopList,
                { expanded = it },
                expanded,
                { selectedIndex = it },
                selectedIndex
            )
        }
        Button(onClick = {
            if (text.text.isNotEmpty()) {
                val shop = shopList.getOrNull(selectedIndex)
                val item = GroceryModel(
                    product = text.text,
                    shop = shop?.id
                )
                onAddGrocery.invoke(item)
            } else {
                textError = true
            }
        }, modifier = Modifier.testTag("saveButton")) {
            Text(text = "Save")
        }
    }


}

@Composable
fun ShopPickerComposable(
    shopList: List<ShopModel>,
    onExpand: (Boolean) -> Unit,
    expanded: Boolean,
    selectedIndexChange: (Int) -> Unit,
    selectedIndex: Int
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .testTag("ShopPickerTag")
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onExpand.invoke(true) })
        ) {
            Text(
                shopList[selectedIndex].shop
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { onExpand.invoke(false) })
        {
            shopList.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndexChange.invoke(index)
                        onExpand.invoke(false)
                    },
                ) {
                    SimpleRow(title = s.shop, Modifier)
                }
            }
        }
    }
}
