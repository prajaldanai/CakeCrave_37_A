package com.example.cakecrave.view.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.cakecrave.model.FavoriteItem
import com.example.cakecrave.navigation.Routes
import com.example.cakecrave.view.BottomNavBar
import com.example.cakecrave.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel
) {
    val favorites by favoritesViewModel.favorites.collectAsState()

    // 👇 Favorites tab index
    var selectedTab by remember { mutableStateOf(2) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Favorites ❤️",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> navController.navigate(Routes.DASHBOARD)
                        1 -> navController.navigate(Routes.DASHBOARD)
                        2 -> { /* already here */ }
                        3 -> navController.navigate(Routes.DASHBOARD)
                    }
                }
            )
        }
    ) { paddingValues ->

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite items yet",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(
                    items = favorites,
                    key = { it.productId }
                ) { item ->
                    FavoriteItemCard(
                        item = item,
                        onDelete = {
                            favoritesViewModel.deleteFavorite(item.productId)
                        },
                        onAddClick = {
                            // ✅ NAVIGATE TO PRODUCT DETAILS
                            navController.navigate(
                                Routes.productDetail(item.productId)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteItemCard(
    item: FavoriteItem,
    onDelete: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2EFF5))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rs. ${item.price}",
                    color = Color(0xFFFF7A00),
                    fontWeight = FontWeight.Bold
                )
            }

            FloatingActionButton(
                onClick = onAddClick, // ✅ FIXED
                modifier = Modifier.size(34.dp),
                containerColor = Color(0xFFFF7A00),
                elevation = FloatingActionButtonDefaults.elevation(2.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "View product",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove favorite",
                    tint = Color.Red
                )
            }
        }
    }
}
