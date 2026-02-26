package com.example.cakecrave

import com.example.cakecrave.model.FavoriteItem
import com.example.cakecrave.repository.FavoriteRepository
import com.example.cakecrave.viewmodel.FavoritesViewModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

class FavoritesViewModelTest {

    // ========== ADD TO FAVORITES ==========
    @Test
    fun addToFavorites_test() {
        val repo = mock<FavoriteRepository>()
        val viewModel = FavoritesViewModel(repo)

        val item = FavoriteItem(
            productId = "p1",
            name = "Red Velvet",
            price = 30.0,
            imageUrl = "https://example.com/rv.jpg"
        )

        viewModel.addToFavorites(item)

        verify(repo).addToFavorites(eq(item))
    }

    // ========== DELETE FAVORITE ==========
    @Test
    fun deleteFavorite_test() {
        val repo = mock<FavoriteRepository>()
        val viewModel = FavoritesViewModel(repo)

        viewModel.deleteFavorite("p1")

        verify(repo).removeFromFavorites(eq("p1"))
    }
}

