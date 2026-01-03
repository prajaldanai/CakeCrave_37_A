package com.example.cakecrave.view

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.model.FavoriteItem
import com.example.cakecrave.viewmodel.FavoritesViewModel

@Composable
fun ProductGridSection(
    products: List<ProductModel>,
    favoritesViewModel: FavoritesViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                product = product,
                favoritesViewModel = favoritesViewModel
            )
        }
    }
}

@Composable
fun ProductCard(
    product: ProductModel,
    favoritesViewModel: FavoritesViewModel
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // ✅ PRODUCT IMAGE — FINAL & GUARANTEED DOUBLE TAP
            AsyncImage(
                model = product.imageUrl.takeIf { it.isNotBlank() },
                contentDescription = product.name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
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
                        )
                    },
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹ ${product.price}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF7A00)
                )

                Spacer(modifier = Modifier.weight(1f))

                FloatingActionButton(
                    onClick = {
                        // cart logic (unchanged)
                    },
                    modifier = Modifier.size(36.dp),
                    containerColor = Color(0xFFFF7A00),
                    shape = CircleShape
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
