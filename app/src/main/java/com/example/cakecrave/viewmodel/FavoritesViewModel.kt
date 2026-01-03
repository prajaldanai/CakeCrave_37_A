package com.example.cakecrave.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.FavoriteItem
import com.example.cakecrave.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel : ViewModel() {

    private val repo = FavoriteRepository()

    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favorites: StateFlow<List<FavoriteItem>> = _favorites

    init {
        repo.listenFavorites { list ->
            _favorites.value = list
        }
    }

    fun addToFavorites(item: FavoriteItem) {
        repo.addToFavorites(item)
    }

    fun deleteFavorite(productId: String) {
        repo.removeFromFavorites(productId)
    }
}
