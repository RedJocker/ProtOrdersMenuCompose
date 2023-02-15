package org.hyperskill.projectname

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
            PlayOrdersMenuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Title(title = "Orders Menu")
                        recipesOnMenuToInitialStockMap.forEach { (recipeName, stockQuantity) ->
                            OrderMenuItem(itemName = recipeName, stockQuantity = stockQuantity)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Title(title: String) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            Text(title, fontSize = 48.sp)
        }
    }

    @Composable
    fun OrderMenuItem(itemName: String, stockQuantity: Int) {
        Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.Bottom) {
            var quantity by remember { mutableStateOf(0) }

            Text(
                text = itemName,
                color = if (quantity >= stockQuantity) Color.Red else Color.Black,
                fontSize = 24.sp
            )

            Text(text = "+", fontSize = 24.sp, modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable {
//                                if(quantity == 1)
//                                    quantity++

                    if (quantity < stockQuantity) {
//                                    quantity += 2
                        quantity++

                    }
                }.testTag(itemName))


            Text(text = "${quantity}", fontSize = 24.sp, modifier = Modifier.testTag(itemName))

            Text(text = "-", fontSize = 24.sp, modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable {
                    if (quantity > 0) {
                        quantity--
                    }
                })
        }
    }
}