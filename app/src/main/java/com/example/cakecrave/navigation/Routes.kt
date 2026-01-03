package com.example.cakecrave.navigation

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"
    const val RESET_PASSWORD = "reset_password"
    const val DASHBOARD = "dashboard"
    const val FAVORITES = "favorites"

    // ✅ PRODUCT DETAIL
    const val PRODUCT_DETAIL = "product_detail/{productId}"

    fun productDetail(productId: String): String {
        return "product_detail/$productId"
    }
}
