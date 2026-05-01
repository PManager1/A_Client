package com.example.birdy.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.birdy.ui.theme.ShimmerBase
import com.example.birdy.ui.theme.ShimmerHighlight

/**
 * A reusable shimmer modifier that creates a left-to-right sliding highlight effect.
 * Uses [ShimmerBase] and [ShimmerHighlight] theme colors for consistency.
 *
 * Usage:
 * ```
 * Box(
 *     modifier = Modifier
 *         .size(100.dp)
 *         .clip(RoundedCornerShape(12.dp))
 *         .shimmer()
 * )
 * ```
 */
fun Modifier.shimmer(
    durationMs: Int = 1000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    val shimmerWidth = 300f
    val travelDistance = 1000f

    this.then(
        Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    ShimmerBase,
                    ShimmerHighlight,
                    ShimmerBase
                ),
                start = Offset(
                    x = progress * travelDistance - shimmerWidth,
                    y = 0f
                ),
                end = Offset(
                    x = progress * travelDistance,
                    y = 0f
                )
            )
        )
    )
}