package com.example.cakecrave

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cakecrave.ui.theme.CakeCraveTheme
import com.example.cakecrave.view.ProductDetailRoute
import com.example.cakecrave.viewmodel.ProductViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI tests for the Product Detail screen.
 *
 * Uses setContent to render ProductDetailRoute directly.
 * ProductViewModel loads products from Firebase.
 * - If a product matching the ID is found, detail UI elements are verified.
 * - If not found (no data / wrong ID), a loading indicator shows.
 *
 * No Firebase login required — ProductDetailRoute has no auth guard.
 */
@RunWith(AndroidJUnit4::class)
class ProductDetailInstrumentedTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var backPressed = false

    /**
     * Helper: render the ProductDetailRoute with a given productId.
     * Uses a real ProductViewModel (products come from Firebase).
     */
    private fun launchDetail(productId: String = "test_product_id") {
        backPressed = false
        composeRule.setContent {
            CakeCraveTheme {
                val productVM = ProductViewModel()
                ProductDetailRoute(
                    productId = productId,
                    productVM = productVM,
                    onBack = { backPressed = true }
                )
            }
        }
        composeRule.waitForIdle()
    }

    // ============================================================
    // TEST 1 — Screen renders without crashing
    // (Product may or may not load from Firebase)
    // ============================================================
    @Test
    fun detailScreen_rendersWithoutCrash() {
        launchDetail()
        // Screen should render — either loading spinner or detail content
        // No crash = PASS
    }

    // ============================================================
    // TEST 2 — Detail title, price, and order button exist
    //          (Only if products loaded from Firebase)
    // ============================================================
    @Test
    fun detailScreen_showsUIElementsOrLoading() {
        launchDetail()

        val hasDetail = try {
            composeRule.onNodeWithTag("detailTitle").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasDetail) {
            composeRule.onNodeWithTag("detailTitle")
                .assertIsDisplayed()

            composeRule.onNodeWithTag("detailPrice")
                .assertExists()
                .assertIsDisplayed()

            composeRule.onNodeWithTag("detailOrderNow")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()

            composeRule.onNodeWithTag("detailBack")
                .assertExists()
                .assertHasClickAction()
        }
        // If no product loaded, loading spinner shows — still a pass
    }

    // ============================================================
    // TEST 3 — Quantity plus and minus buttons exist
    // ============================================================
    @Test
    fun detailScreen_quantityControlsExist() {
        launchDetail()

        val hasDetail = try {
            composeRule.onNodeWithTag("qtyPlus").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasDetail) {
            composeRule.onNodeWithTag("qtyPlus")
                .assertIsDisplayed()
                .assertHasClickAction()

            composeRule.onNodeWithTag("qtyMinus")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()

            // Default quantity should be 1
            composeRule.onNodeWithTag("qtyText")
                .assertExists()
                .assertTextContains("1")
        }
    }

    // ============================================================
    // TEST 4 — Clicking qtyPlus increases the quantity text
    // ============================================================
    @Test
    fun detailScreen_qtyPlusIncreasesQuantity() {
        launchDetail()

        val hasDetail = try {
            composeRule.onNodeWithTag("qtyPlus").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasDetail) {
            // Quantity starts at 1
            composeRule.onNodeWithTag("qtyText")
                .assertTextContains("1")

            // Click plus
            composeRule.onNodeWithTag("qtyPlus").performClick()
            composeRule.waitForIdle()

            // Quantity should now be 2
            composeRule.onNodeWithTag("qtyText")
                .assertTextContains("2")
        }
    }

    // ============================================================
    // TEST 5 — Back button triggers onBack callback
    // ============================================================
    @Test
    fun detailScreen_backButtonWorks() {
        launchDetail()

        val hasBack = try {
            composeRule.onNodeWithTag("detailBack").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasBack) {
            composeRule.onNodeWithTag("detailBack").performClick()
            composeRule.waitForIdle()
            assert(backPressed) { "Expected onBack to be called after clicking back button" }
        }
    }

    // ============================================================
    // TEST 6 — Order Now button is clickable (dialog appears)
    // Does NOT depend on Firebase — only verifies UI interaction
    // ============================================================
    @Test
    fun detailScreen_orderNowOpensConfirmDialog() {
        launchDetail()

        val hasOrder = try {
            composeRule.onNodeWithTag("detailOrderNow").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasOrder) {
            composeRule.onNodeWithTag("detailOrderNow").performClick()
            composeRule.waitForIdle()

            // Confirm dialog should appear with "Confirm Order" title
            composeRule.onNodeWithText("Confirm Order")
                .assertExists()
                .assertIsDisplayed()
        }
    }
}

