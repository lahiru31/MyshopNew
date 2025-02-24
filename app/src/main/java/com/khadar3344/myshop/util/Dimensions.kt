package com.khadar3344.myshop.util

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimensions {
    private val screenWidth
        @Composable
        @ReadOnlyComposable
        get() = LocalConfiguration.current.screenWidthDp.dp

    private val screenHeight
        @Composable
        @ReadOnlyComposable
        get() = LocalConfiguration.current.screenHeightDp.dp

    // Spacing values
    val spacing_small
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.02f).coerceAtLeast(4.dp)

    val spacing_medium
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.04f).coerceAtLeast(8.dp)

    val spacing_large
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.06f).coerceAtLeast(16.dp)

    val spacing_xlarge
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.08f).coerceAtLeast(32.dp)

    // Text sizes
    val text_small
        @Composable
        @ReadOnlyComposable
        get() = (12.sp * screenWidth / 360.dp).coerceAtLeast(10.sp)

    val text_medium
        @Composable
        @ReadOnlyComposable
        get() = (14.sp * screenWidth / 360.dp).coerceAtLeast(12.sp)

    val text_large
        @Composable
        @ReadOnlyComposable
        get() = (16.sp * screenWidth / 360.dp).coerceAtLeast(14.sp)

    val text_xlarge
        @Composable
        @ReadOnlyComposable
        get() = (20.sp * screenWidth / 360.dp).coerceAtLeast(18.sp)

    val text_title
        @Composable
        @ReadOnlyComposable
        get() = (24.sp * screenWidth / 360.dp).coerceAtLeast(20.sp)

    // Component sizes
    val button_height
        @Composable
        @ReadOnlyComposable
        get() = (screenHeight * 0.07f).coerceAtLeast(40.dp)

    val input_field_height
        @Composable
        @ReadOnlyComposable
        get() = (screenHeight * 0.08f).coerceAtLeast(48.dp)

    val image_size_small
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.1f).coerceAtLeast(32.dp)

    val image_size_medium
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.2f).coerceAtLeast(64.dp)

    val image_size_large
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth * 0.3f).coerceAtLeast(96.dp)

    // Grid columns based on screen width
    val gridColumns
        @Composable
        @ReadOnlyComposable
        get() = when {
            screenWidth < 600.dp -> 2
            screenWidth < 840.dp -> 3
            else -> 4
        }

    // Dynamic padding for different screen sizes
    val screenPadding
        @Composable
        @ReadOnlyComposable
        get() = when {
            screenWidth < 600.dp -> 16.dp
            screenWidth < 840.dp -> 24.dp
            else -> 32.dp
        }

    // Helper function to calculate responsive dimensions
    fun Dp.responsive(): Dp {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
        val factor = screenWidth / 360f // Base width
        return (this * factor).coerceAtLeast(this)
    }
}
