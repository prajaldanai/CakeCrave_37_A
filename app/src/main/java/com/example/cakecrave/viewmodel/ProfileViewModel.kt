package com.example.cakecrave.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    private val repo = ProductRepository()

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    init {
        repo.observeProducts {
            _products.value = it
        }
    }

    fun addProduct(
        name: String,
        price: Double,
        description: String,
        category: String,
        imageUri: Uri
    ) {
        repo.addProduct(name, price, description, category, imageUri)
    }

    fun deleteProduct(product: ProductModel) {
        repo.deleteProduct(product)
    }
}
