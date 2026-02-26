package com.example.cakecrave

import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.repository.ProductRepository
import com.example.cakecrave.viewmodel.ProductViewModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

class ProductViewModelTest {

    // ========== ADD PRODUCT SUCCESS ==========
    @Test
    fun addProduct_success_test() {
        val repo = mock<ProductRepository>()
        val viewModel = ProductViewModel(repo)

        viewModel.addProduct(
            name = "Chocolate Cake",
            price = 25.0,
            description = "Delicious chocolate cake",
            category = "cakes",
            imageUrl = "https://example.com/cake.jpg"
        )

        assertEquals("Product added successfully", viewModel.message.value)
        verify(repo).addProduct(
            eq("Chocolate Cake"),
            eq(25.0),
            eq("Delicious chocolate cake"),
            eq("cakes"),
            eq("https://example.com/cake.jpg")
        )
    }

    // ========== DELETE PRODUCT ==========
    @Test
    fun deleteProduct_test() {
        val repo = mock<ProductRepository>()
        val viewModel = ProductViewModel(repo)

        viewModel.deleteProduct("product123")

        assertEquals("Product deleted", viewModel.message.value)
        verify(repo).deleteProduct(eq("product123"))
    }
}

