package org.hyperskill.projectname

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.playcomposecounter.ui.theme.PlayOrdersMenuTheme

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
                        Row(horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Button(
                                onClick = {
                                    val orderedRecipesString =
                                        mapQuantitiesOrdered.mapNotNull { (recipeName, quantity) ->
                                            if(quantity > 0) "${recipeName}: $quantity" else null
                                        }.joinToString(separator = "\n")
                                    if (orderedRecipesString.isNotBlank()) {
                                        mapStock = mapStock.mapValues { (recipeName, stockAmount) ->
                                            stockAmount - mapQuantitiesOrdered[recipeName]!!
                                        }

                                        mapQuantitiesOrdered = mapQuantitiesOrdered.mapValues { 0 }
                                        val message = "Ordered:\n$orderedRecipesString"
                                        showToast(message)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Black,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Make Order",
                                    fontSize = 24.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Activity.showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    @Composable
    fun Title(title: String) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            Text(title, fontSize = 48.sp)
        }
    }

    @Composable
    fun OrderMenuItem(itemName: String,
                      quantityOrdered: Int,
                      stockLimit: Int,
                      onAddAmountOrdered: (currentQuantity: Int) -> Unit,
                      onDecreaseAmountOrdered: (currentQuantity: Int) -> Unit) {

        Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.Bottom) {

            Text(
                text = itemName,
                color = if (quantityOrdered >= stockLimit) Color.Red else Color.Black,
                fontSize = 24.sp
            )

            Text(text = "+", fontSize = 24.sp, modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable {
                    onAddAmountOrdered(quantityOrdered)
                }
                .testTag(itemName))


            Text(text = "${quantityOrdered}", fontSize = 24.sp, modifier = Modifier.testTag(itemName))

            Text(text = "-", fontSize = 24.sp, modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable {
                    onDecreaseAmountOrdered(quantityOrdered)
                }
            )
        }
    }
}