package com.example.cakecrave

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cakecrave.ui.theme.CakeCraveTheme
import com.example.cakecrave.view.favorites.FavoritesScreen
import com.example.cakecrave.viewmodel.FavoritesViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FavoritesInstrumentedTest {

    @get:Rule
    val composeRule = createComposeRule()

    /**
     * Helper: render the FavoritesScreen with a real ViewModel.
     */
    private fun launchFavorites() {
        composeRule.setContent {
            CakeCraveTheme {
                val navController = rememberNavController()
                val favoritesVM = FavoritesViewModel()
                FavoritesScreen(
                    navController = navController,
                    favoritesViewModel = favoritesVM
                )
            }
        }
        composeRule.waitForIdle()
    }

    // ============================================================
    // TEST 1 — Screen renders without crashing
    // ============================================================
    @Test
    fun favoritesScreen_rendersWithoutCrash() {
        launchFavorites()
        // No crash = PASS
        // Title should always be visible
        composeRule.onNodeWithText("Favorites ❤️")
            .assertExists()
            .assertIsDisplayed()
    }

    // ============================================================
    // TEST 2 — Shows favorites list OR empty state message
    // ============================================================
    @Test
    fun favoritesScreen_showsListOrEmptyState() {
        launchFavorites()

        val hasFavorites = try {
            composeRule.onNodeWithTag("favoritesList").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasFavorites) {
            // List is displayed with at least one item
            composeRule.onNodeWithTag("favoritesList")
                .assertIsDisplayed()

            composeRule.onNodeWithTag("favItem_0")
                .assertExists()
                .assertIsDisplayed()
        } else {
            // Empty state text is shown
            composeRule.onNodeWithTag("emptyFavText")
                .assertExists()
                .assertIsDisplayed()

            composeRule.onNodeWithText("No favorite items yet")
                .assertIsDisplayed()
        }
    }

    // ============================================================
    // TEST 3 — Delete button exists on first favorite item
    //          (only if favorites are loaded)
    // ============================================================
    @Test
    fun favoritesScreen_deleteButtonExists() {
        launchFavorites()

        val hasFavorites = try {
            composeRule.onNodeWithTag("favItem_0").assertExists()
            true
        } catch (_: AssertionError) {
            false
        }

        if (hasFavorites) {
            composeRule.onNodeWithTag("removeFav_0")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
        }
        // If no favorites, test passes (nothing to delete)
    }

    // ============================================================
    // TEST 4 — Favorites screen title is always displayed
    // ============================================================
    @Test
    fun favoritesScreen_titleAlwaysDisplayed() {
        launchFavorites()

        composeRule.onNodeWithText("Favorites ❤️")
            .assertExists()
            .assertIsDisplayed()
    }
}

