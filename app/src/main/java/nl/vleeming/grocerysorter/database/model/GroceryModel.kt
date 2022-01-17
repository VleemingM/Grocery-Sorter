package nl.vleeming.grocerysorter.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(
    tableName = "grocery_table",
    foreignKeys = [ForeignKey(
        entity = ShopModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("shop"),
        onDelete = ForeignKey.SET_NULL
    )]
)
data class GroceryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "product")
    val product: String,
    @ColumnInfo(index = true)
    val shop: String? = null
)
