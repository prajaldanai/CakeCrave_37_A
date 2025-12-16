package com.example.cakecrave.view

// ---------- Android ----------
import android.content.Intent
import android.net.Uri

// ---------- Activity Result ----------
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

// ---------- Compose ----------
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

// ---------- Layout ----------
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// ---------- Material ----------
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

// ---------- Image ----------
import coil.compose.AsyncImage

// ---------- Context ----------
import androidx.compose.ui.platform.LocalContext

// ---------- ViewModel ----------
import com.example.cakecrave.viewmodel.ProductViewModel
import com.example.cakecrave.model.ProductModel

@Composable
fun AddProductScreen(
    productVM: ProductViewModel
) {

    val context = LocalContext.current

    // ================= FORM STATE =================
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("cake") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ================= OBSERVE PRODUCTS =================
    val products by productVM.products.collectAsState()

    // ================= IMAGE PICKER (PERSISTENT) =================
    val imagePicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = it
            }
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ================= ADD PRODUCT FORM =================
        item {

            Text(
                text = "Add Product",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = { imagePicker.launch(arrayOf("image/*")) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (imageUri == null) "Pick Image" else "Image Selected")
            }

            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // ================= CATEGORY =================
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryRadio("cake", category) { category = it }
                CategoryRadio("donut", category) { category = it }
                CategoryRadio("cookie", category) { category = it }
            }

            // ================= ADD BUTTON =================
            Button(
                onClick = {

                    if (
                        name.isBlank() ||
                        price.isBlank() ||
                        description.isBlank() ||
                        imageUri == null
                    ) return@Button

                    productVM.addProduct(
                        name = name,
                        price = price.toDouble(),
                        description = description,
                        category = category.lowercase(),
                        imageUri = imageUri!!
                    )

                    // Reset form
                    name = ""
                    price = ""
                    description = ""
                    category = "cake"
                    imageUri = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
        }

        // ================= PRODUCT LIST BELOW =================
        items(products) { product ->
            ProductRow(
                product = product,
                onDelete = { productVM.deleteProduct(product) }
            )
        }
    }
}

// ================= PRODUCT ROW =================
@Composable
private fun ProductRow(
    product: ProductModel,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.size(64.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(product.name)
                Text("Rs ${product.price}")
                Text(product.category)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

// ================= CATEGORY RADIO =================
@Composable
fun CategoryRadio(
    value: String,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = value == selected,
            onClick = { onSelect(value) }
        )
        Text(value.replaceFirstChar { it.uppercase() })
    }
}
