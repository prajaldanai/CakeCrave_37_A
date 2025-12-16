package com.example.cakecrave.view

// ================= IMPORTS =================
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cakecrave.viewmodel.ProductViewModel

// ================= HOME CONTENT =================
@Composable
fun HomeContent(
    productVM: ProductViewModel,
    modifier: Modifier = Modifier
) {

    // 🔥 Observe products from ViewModel
    val products by productVM.products.collectAsState()

    // Category state
    var selectedCategory by remember { mutableStateOf("cake") }

    // Filter products by category
    val filteredProducts = products.filter {
        it.category.equals(selectedCategory, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // ================= CATEGORY SECTION =================
        CategorySection(
            selected = selectedCategory,
            onSelect = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ================= PRODUCT LIST =================
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No products found")
            }
        } else {
            ProductGridSection(
                products = filteredProducts
            )
        }
    }
}
