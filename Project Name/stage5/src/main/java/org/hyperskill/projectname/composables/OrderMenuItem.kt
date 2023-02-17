package org.hyperskill.projectname.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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