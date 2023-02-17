package org.hyperskill.projectname

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.example.playcomposecounter.ui.theme.PlayOrdersMenuTheme
import org.hyperskill.projectname.composables.MakeOrderButton
import org.hyperskill.projectname.composables.OrderMenuItem
import org.hyperskill.projectname.composables.Title

class MainActivity : ComponentActivity() {

    companion object {
        private val recipesOnMenuToInitialStockMap = mapOf(
            "Fettuccine" to 5,
            "Risotto" to 6,
            "Gnocchi" to 4,
            "Spaghetti" to 3,
            "Lasagna" to 5,
            "Steak Parmigiana" to 2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var mapQuantitiesOrdered by remember {
                // initial ordered quantity is 0 for every item
                mutableStateOf(recipesOnMenuToInitialStockMap.mapValues { 0 })
            }

            var mapStock by remember {
                mutableStateOf(recipesOnMenuToInitialStockMap)
            }

            PlayOrdersMenuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Title(title = "Orders Menu")
                        recipesOnMenuToInitialStockMap.forEach { (recipeName, _) ->
                            OrderMenuItem(
                                itemName = recipeName,
                                quantityOrdered = mapQuantitiesOrdered[recipeName]!!,
                                stockLimit = mapStock[recipeName]!!,
                                onAddAmountOrdered = { currentQuantity ->
                                    val stockLimit = mapStock[recipeName]!!
                                    val increasedQuantity =
                                        if(currentQuantity >= stockLimit)
                                            stockLimit
                                        else currentQuantity + 1
                                    mapQuantitiesOrdered = mapQuantitiesOrdered + (recipeName to increasedQuantity)

                                },
                                onDecreaseAmountOrdered = { currentQuantity ->
                                    val decreasedQuantity =
                                        if(currentQuantity <= 0)
                                            0
                                        else currentQuantity - 1
                                    mapQuantitiesOrdered = mapQuantitiesOrdered + (recipeName to decreasedQuantity)
                                }
                            )
                        }
                        MakeOrderButton(
                            onClick = {
                                val orderedRecipesString =
                                    mapQuantitiesOrdered.mapNotNull { (recipeName, quantity) ->
                                        if (quantity > 0) "==> ${recipeName}: $quantity" else null
                                    }.joinToString(separator = "\n")
                                if (orderedRecipesString.isNotBlank()) {
                                    mapStock = mapStock.mapValues { (recipeName, stockAmount) ->
                                        stockAmount - mapQuantitiesOrdered[recipeName]!!
                                    }

                                    mapQuantitiesOrdered = mapQuantitiesOrdered.mapValues { 0 }
                                    val message = "Ordered:\n$orderedRecipesString"
                                    showToast(message)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun Activity.showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}