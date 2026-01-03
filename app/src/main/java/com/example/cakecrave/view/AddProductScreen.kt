package com.example.cakecrave.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(productVM: ProductViewModel) {

    val products by productVM.products.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    var editProduct by remember { mutableStateOf<ProductModel?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editProduct = null
                showSheet = true
            }) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.id }) { product ->
                ProductRow(
                    product = product,
                    onDelete = { productVM.deleteProduct(product.id) },
                    onEdit = {
                        editProduct = product
                        showSheet = true
                    }
                )
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            AddEditProductSheet(
                productVM = productVM,
                product = editProduct,
                onClose = { showSheet = false }
            )
        }
    }
}

/* ================= ADD / EDIT SHEET ================= */

@Composable
private fun AddEditProductSheet(
    productVM: ProductViewModel,
    product: ProductModel?,
    onClose: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "cake") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            selectedImageUri = it
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Pick Image")
                }
            }

            item {
                val preview = selectedImageUri ?: product?.imageUrl
                preview?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Text("Category", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CategoryChip("Cake", "cake", category) { category = it }
                    CategoryChip("Donut", "donut", category) { category = it }
                    CategoryChip("Cookie", "cookie", category) { category = it }
                }
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    minLines = 4,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Divider()

        Button(
            enabled = !uploading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(54.dp),
            onClick = {
                focusManager.clearFocus()

                val priceValue = price.toDoubleOrNull()
                if (priceValue == null) {
                    error = "Invalid price"
                    return@Button
                }

                if (selectedImageUri != null) {
                    uploading = true
                    productVM.uploadImage(selectedImageUri) { url ->
                        uploading = false
                        if (url != null) {
                            saveProduct(
                                productVM,
                                product,
                                name,
                                priceValue,
                                description,
                                category,
                                url
                            )
                            onClose()
                        } else {
                            error = "Image upload failed"
                        }
                    }
                } else {
                    saveProduct(
                        productVM,
                        product,
                        name,
                        priceValue,
                        description,
                        category,
                        product?.imageUrl ?: ""
                    )
                    onClose()
                }
            }
        ) {
            Text(if (uploading) "Saving..." else "Save")
        }
    }
}

/* ================= CATEGORY CHIP ================= */

@Composable
private fun CategoryChip(
    label: String,
    value: String,
    selected: String,
    onSelect: (String) -> Unit
) {
    val isSelected = value == selected

    Box(
        modifier = Modifier
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(20.dp)
            )
            .clickable { onSelect(value) }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

/* ================= SAVE LOGIC ================= */

private fun saveProduct(
    vm: ProductViewModel,
    existing: ProductModel?,
    name: String,
    price: Double,
    desc: String,
    category: String,
    imageUrl: String
) {
    if (existing == null) {
        vm.addProduct(name, price, desc, category, imageUrl)
    } else {
        vm.updateProduct(
            existing.copy(
                name = name,
                price = price,
                description = desc,
                category = category,
                imageUrl = imageUrl
            )
        )
    }
}

/* ================= PRODUCT ROW ================= */

@Composable
private fun ProductRow(
    product: ProductModel,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("₹ ${product.price}")
                Text(product.category, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, null)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null)
            }
        }
    }
}
