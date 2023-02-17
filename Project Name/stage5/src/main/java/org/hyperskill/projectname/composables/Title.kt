package org.hyperskill.projectname.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun Title(title: String) {
    Row(Modifier.fillMaxWidth(), Arrangement.Center) {
        Text(title, fontSize = 48.sp)
    }
}