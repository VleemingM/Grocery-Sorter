package nl.vleeming.grocerysorter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.screen.DrawerScreen
import nl.vleeming.grocerysorter.ui.theme.GrocerySorterTheme
import nl.vleeming.grocerysorter.viewmodel.AddGroceryViewModel

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
fun MainScreen(groceryViewModel: AddGroceryViewModel = hiltViewModel()) {
    val list = groceryViewModel.groceries.observeAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {BasicText(text = "Groceries")},

                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isOpen) close() else open()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")}
                    }

            )

        },
        drawerContent = { DrawerScreen() },
        floatingActionButton = { AddGroceryFab() },
        content = { GroceryList(groceryList = list.value) }
    )
}

@Composable
fun GroceryList(groceryList: List<GroceryModel>) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        groceryList.forEach { grocery ->
            GroceryRow(grocery)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryRowPreview() {
    GroceryRow(GroceryModel(product = "DIT IS EEN PRODUCT :-)"))
}

@Composable
fun GroceryRow(grocery: GroceryModel) {
    Column {
        BasicText(
            text = grocery.product,
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
                }) { Text(text = "Never mind") }
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
    // TODO: VLEEMING 18/10/2021 add a selector for shop here

}



