package com.example.cakecrave.view

import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.cakecrave.model.FavoriteItem
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.navigation.Routes
import com.example.cakecrave.viewmodel.FavoritesViewModel
import com.example.cakecrave.viewmodel.ProductViewModel

@Composable
fun HomeContent(
    navController: NavHostController,            // ✅ ADDED
    productVM: ProductViewModel,
    favoritesViewModel: FavoritesViewModel,
    modifier: Modifier = Modifier
) {
    val products by productVM.products.collectAsState()
    var selectedCategory by remember { mutableStateOf("cake") }

    val filteredProducts = remember(products, selectedCategory) {
        products.filter {
            it.category.equals(selectedCategory, ignoreCase = true)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {

        // ================= CATEGORY HEADER =================
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                CategorySection(
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // ================= EMPTY STATE =================
        if (filteredProducts.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No products found")
                }
            }
        } else {

            // ================= PRODUCT GRID =================
            items(
                items = filteredProducts,
                key = { it.id }
            ) { product ->
                ProductGridCard(
                    product = product,
                    favoritesViewModel = favoritesViewModel,
                    onAddClick = {
                        // ✅ NAVIGATE TO PRODUCT DETAILS
                        navController.navigate(
                            Routes.productDetail(product.id)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductGridCard(
    product: ProductModel,
    favoritesViewModel: FavoritesViewModel,
    onAddClick: () -> Unit                // ✅ ADDED
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2EFF5))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // ❤️ DOUBLE TAP TO FAVORITE
            AsyncImage(
                model = product.imageUrl.takeIf { it.isNotBlank() },
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .combinedClickable(
                        onClick = {}, // keep empty (card tap)
                        onDoubleClick = {
                            favoritesViewModel.addToFavorites(
                                FavoriteItem(
                                    productId = product.id,
                                    name = product.name,
                                    price = product.price,
                                    imageUrl = product.imageUrl
                                )
                            )

                            Toast.makeText(
                                context,
                                "Added to favorites ❤️",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Rs. ${product.price}",
                    color = Color(0xFFFF7A00),
                    fontWeight = FontWeight.Bold
                )

                FloatingActionButton(
                    onClick = onAddClick,     // ✅ FIXED
                    modifier = Modifier.size(34.dp),
                    containerColor = Color(0xFFFF7A00),
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
