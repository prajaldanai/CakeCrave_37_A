package com.example.cakecrave.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategorySection(
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CategoryChip(
            title = "cake",
            icon = Icons.Default.Cake,
            selected = selected,
            onSelect = onSelect
        )

        CategoryChip(
            title = "donut",
            icon = Icons.Default.DonutLarge,
            selected = selected,
            onSelect = onSelect
        )

        CategoryChip(
            title = "cookie",
            icon = Icons.Default.Cookie,
            selected = selected,
            onSelect = onSelect
        )
    }
}

@Composable
private fun CategoryChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: String,
    onSelect: (String) -> Unit
) {
    val isActive = title == selected

    val backgroundColor =
        if (isActive) Color(0xFFFF7A00) else Color(0xFFF3F3F3)

    val contentColor =
        if (isActive) Color.White else Color(0xFF9E9E9E)

    Row(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(50))
            .clickable { onSelect(title) }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title.replaceFirstChar { it.uppercase() },
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
