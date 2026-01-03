package com.example.cakecrave.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.cakecrave.model.OrderModel
import com.example.cakecrave.viewmodel.OrderViewModel
import com.example.cakecrave.viewmodel.ProductViewModel
import com.example.cakecrave.viewmodel.UserViewModel

@Composable
fun ProductDetailRoute(
    productId: String,
    productVM: ProductViewModel,
    onBack: () -> Unit
) {
    val products by productVM.products.collectAsState()
    val product = products.find { it.id == productId }

    val userVM: UserViewModel = viewModel()
    val orderVM: OrderViewModel = viewModel()

    var quantity by remember { mutableStateOf(1) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var orderSuccess by remember { mutableStateOf(false) }
    var isPlacingOrder by remember { mutableStateOf(false) }

    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val totalPrice = product.price * quantity

    /* ================= CONFIRM DIALOG ================= */

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { if (!isPlacingOrder) showConfirmDialog = false },
            title = { Text("Confirm Order") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(product.name, fontWeight = FontWeight.Bold)
                    Text("Quantity: $quantity")
                    Text(
                        "Total: ₹ $totalPrice",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF7A00)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isPlacingOrder,
                    onClick = {
                        if (isPlacingOrder) return@TextButton
                        isPlacingOrder = true

                        userVM.getUserProfile { profile ->
                            if (profile == null ||
                                profile.name.isBlank() ||
                                profile.phone.isBlank()
                            ) {
                                isPlacingOrder = false
                                showConfirmDialog = false
                                return@getUserProfile
                            }

                            val order = OrderModel(
                                userName = profile.name,
                                userPhone = profile.phone,
                                productId = product.id,
                                productName = product.name,
                                price = product.price,
                                quantity = quantity,
                                totalPrice = totalPrice,
                                imageUrl = product.imageUrl
                            )

                            orderVM.placeOrder(
                                order = order,
                                onSuccess = {
                                    isPlacingOrder = false
                                    showConfirmDialog = false
                                    orderSuccess = true
                                },
                                onError = {
                                    isPlacingOrder = false
                                    showConfirmDialog = false
                                }
                            )
                        }
                    }
                ) {
                    if (isPlacingOrder) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text("Confirm")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isPlacingOrder,
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (orderSuccess) {
        AlertDialog(
            onDismissRequest = { orderSuccess = false },
            title = { Text("🎉 Order Placed") },
            text = { Text("Your order has been placed successfully!") },
            confirmButton = {
                TextButton(onClick = {
                    orderSuccess = false
                    onBack()
                }) {
                    Text("Go to Home")
                }
            }
        )
    }

    /* ================= MAIN LAYOUT ================= */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FB))
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "Product Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))
                }
            }

            item {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 28.dp,
                                bottomEnd = 28.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = product.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = product.description,
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        lineHeight = 22.sp
                    )

                    Text("Price", fontSize = 14.sp, color = Color.Gray)

                    Text(
                        text = "₹ ${product.price}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF7A00)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Quantity", fontSize = 16.sp)

                        Spacer(Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFF1F1F1), CircleShape)
                            ) {
                                Icon(Icons.Default.Remove, null)
                            }

                            Text(
                                text = quantity.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFFF7A00), CircleShape)
                            ) {
                                Icon(Icons.Default.Add, null, tint = Color.White)
                            }
                        }
                    }
                }
            }
        }

        /* ================= STICKY BOTTOM BAR ================= */

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        "₹ $totalPrice",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF7A00)
                    )
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { showConfirmDialog = true },
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7A00)
                    )
                ) {
                    Text(
                        "Order Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
