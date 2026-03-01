package com.example.cakecrave.view

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.example.cakecrave.model.FavoriteItem
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.navigation.Routes
import com.example.cakecrave.viewmodel.FavoritesViewModel
import com.example.cakecrave.viewmodel.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        modifier = modifier
            .padding(horizontal = 16.dp)
            .testTag("productList"),
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
                val index = filteredProducts.indexOf(product)
                ProductGridCard(
                    product = product,
                    favoritesViewModel = favoritesViewModel,
                    index = index,
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
    index: Int = 0,
    onAddClick: () -> Unit
) {
    val context = LocalContext.current
    var showHeart by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2EFF5)),
        modifier = Modifier.testTag("productItem_$index")
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // ❤️ DOUBLE TAP TO FAVORITE — using pointerInput for reliable detection
            Box(
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(
                            product.imageUrl
                                .takeIf { it.isNotBlank() }
                                ?.replace("http://", "https://")
                        )
                        .build(),
                    contentDescription = product.name,
                    modifier = Modifier
                        .height(120.dp)
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
                                    showHeart = true
                                    scope.launch {
                                        delay(900)
                                        showHeart = false
                                    }
                                    Toast.makeText(
                                        context,
                                        "${product.name} added to favorites ❤️",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        },
                    contentScale = ContentScale.Crop
                )

                // ❤️ heart animation overlay
                if (showHeart) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorited",
                        tint = Color.Red.copy(alpha = 0.85f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

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
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("openDetails_$index"),
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
