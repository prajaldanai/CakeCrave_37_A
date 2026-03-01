package com.example.cakecrave.repository

import com.example.cakecrave.model.FavoriteItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

open class FavoriteRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    private fun favRef(): DatabaseReference? {
        val uid = auth.currentUser?.uid
        return uid?.let {
            db.child("favorites").child(it)
        }
    }

    open fun addToFavorites(item: FavoriteItem) {
        favRef()?.child(item.productId)?.setValue(item)
    }

    open fun listenFavorites(onResult: (List<FavoriteItem>) -> Unit) {
        favRef()?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(FavoriteItem::class.java)
                }.map { item ->
                    // ✅ Fix old HTTP URLs → HTTPS so Coil/Android can load them
                    item.copy(imageUrl = item.imageUrl.ensureHttps())
                }
                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    open fun removeFromFavorites(productId: String) {
        favRef()?.child(productId)?.removeValue()
    }

    /** Converts http:// Cloudinary URLs to https:// */
    private fun String.ensureHttps(): String {
        return if (startsWith("http://")) replace("http://", "https://") else this
    }
}
