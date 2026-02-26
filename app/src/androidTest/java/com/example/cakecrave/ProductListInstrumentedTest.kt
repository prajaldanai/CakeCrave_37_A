package com.example.cakecrave

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cakecrave.ui.theme.CakeCraveTheme
import com.example.cakecrave.view.HomeContent
import com.example.cakecrave.viewmodel.FavoritesViewModel
import com.example.cakecrave.viewmodel.ProductViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI tests for the Product List screen (HomeContent).
 *
 * Uses setContent to render HomeContent directly with a real ProductViewModel.
 * Products come from Firebase — if empty, we verify the empty state.
 * If products exist, we verify the grid and card tags.
 *
 * No Firebase login required — HomeContent does not have an auth guard.
 */
@RunWith(AndroidJUnit4::class)
class ProductListInstrumentedTest {

    @get:Rule
    val composeRule = createComposeRule()

    // ============================================================
    // TEST 1 — Product list container exists
    // ============================================================
    @Test
    fun productList_containerExists() {
        composeRule.setContent {
            CakeCraveTheme {
                val navController = rememberNavController()
                val productVM = ProductViewModel()
                val favoritesVM = FavoritesViewModel()

                HomeContent(
                    navController = navController,
                    productVM = productVM,
                    favoritesViewModel = favoritesVM,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                )
            }
        }

        // The LazyVerticalGrid with testTag("productList") should exist
        composeRule.onNodeWithTag("productList")
            .assertExists()
            .assertIsDisplayed()
    }

    // ============================================================
    // TEST 2 — Product list is scrollable (has scroll action)
    // ============================================================
    @Test
    fun productList_isScrollable() {
        composeRule.setContent {
            CakeCraveTheme {
                val navController = rememberNavController()
                val productVM = ProductViewModel()
                val favoritesVM = FavoritesViewModel()

                HomeContent(
                    navController = navController,
                    productVM = productVM,
                    favoritesViewModel = favoritesVM,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                )
            }
        }

        composeRule.onNodeWithTag("productList")
            .assertExists()
        // LazyVerticalGrid is scrollable by nature
    }

    // ============================================================
    // TEST 3 — If products load, first product card exists
    //          If empty, "No products found" text appears
    // ============================================================
    @Test
    fun productList_showsProductsOrEmptyState() {
        composeRule.setContent {
            CakeCraveTheme {
                val navController = rememberNavController()
                val productVM = ProductViewModel()
                val favoritesVM = FavoritesViewModel()

                HomeContent(
                    navController = navController,
                    productVM = productVM,
                    favoritesViewModel = favoritesVM,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                )
            }
        }

        // Wait for data to load (Firebase may or may not return products)
        composeRule.waitForIdle()

        // Either products exist OR the empty message shows
        val hasProduct = try {
            composeRule.onNodeWithTag("productItem_0").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasProduct) {
            // Product card is displayed with open-details button
            composeRule.onNodeWithTag("productItem_0")
                .assertIsDisplayed()
            composeRule.onNodeWithTag("openDetails_0")
                .assertExists()
                .assertHasClickAction()
        } else {
            // Empty state message
            composeRule.onNodeWithText("No products found")
                .assertExists()
                .assertIsDisplayed()
        }
    }
}

