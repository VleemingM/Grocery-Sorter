package nl.vleeming.grocerysorter.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nl.vleeming.grocerysorter.database.model.GroceryModel
import nl.vleeming.grocerysorter.screen.GroceryList

private val DarkColorPalette = darkColors(
    primary = Pink300Dark,
    primaryVariant = Pink300,
    secondary = Teal300Dark
)

private val LightColorPalette = lightColors(
    primary = Pink300Light,
    primaryVariant = Pink300,
    secondary = Teal300Light

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun GrocerySorterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun GroceryItemPreview() {
    MaterialTheme {
        GroceryList(
            groceryList =
            listOf(
                GroceryModel(0, "Brood"),
                GroceryModel(0, "Melk"),
                GroceryModel(0, "Tofu"),
                GroceryModel(0, "Kaas")
            )
        )
    }

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GroceryItemDarkModePreview() {
    GroceryItemPreview()
}