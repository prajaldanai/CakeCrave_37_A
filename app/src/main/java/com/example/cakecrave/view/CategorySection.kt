package com.example.cakecrave.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategorySection(
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryChip("cake", selected, onSelect)
        CategoryChip("donut", selected, onSelect)
        CategoryChip("cookie", selected, onSelect)
    }
}

@Composable
fun CategoryChip(
    title: String,
    selected: String,
    onSelect: (String) -> Unit
) {
    val active = title == selected

    Box(
        modifier = Modifier
            .background(
                if (active) Color(0xFFFF9800) else Color(0xFFF2F2F2),
                RoundedCornerShape(50)
            )
            .clickable { onSelect(title) }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = title.replaceFirstChar { it.uppercase() },
            color = if (active) Color.White else Color.Black
        )
    }
}
